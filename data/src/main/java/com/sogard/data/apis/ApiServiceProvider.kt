package com.sogard.data.apis

import com.sogard.data.models.*
import com.sogard.data.network.AppNetworkConfiguration.BASE_URL
import com.sogard.data.network.AppNetworkConfiguration.BASE_URL_PUBLIC
import com.sogard.data.network.AuthorizationServiceTokenInterceptor
import com.sogard.data.network.DefaultTokenInterceptor
import com.sogard.data.network.RedditAuthenticator
import com.sogard.data.network.TokenManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 *  The class in charge with creating API services.
 */
class ApiServiceGenerator(authenticationCall: () -> Single<String>, tokenManager: TokenManager) {

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    /**
     *  Json to Kotlin/Java (de)serialization client.
     *
     *  Note:   Since the API works with a lot of wrappers and generic data, a PolymorphicJsonAdapterFactory
     *          is used to simplify the serialization process.
     */
    private val moshiClient = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(CoreRedditDAO::class.java, KEY_WRAPPER_TYPE)
                .withSubtype(ListingData::class.java, DataWrapperType.Listing.name)
                .withSubtype(ArticleDAO::class.java, DataWrapperType.t3.name)
                .withSubtype(CommentDAO::class.java, DataWrapperType.t1.name)
                .withSubtype(MoreDataDAO::class.java, DataWrapperType.more.name)
        )
        .add(
            PolymorphicJsonAdapterFactory.of(DataWrapper::class.java, KEY_WRAPPER_TYPE)
                .withSubtype(ListingWrapper::class.java, DataWrapperType.Listing.name)
                .withSubtype(ArticleWrapper::class.java, DataWrapperType.t3.name)
                .withSubtype(CommentWrapper::class.java, DataWrapperType.t1.name)
                .withSubtype(MoreDataWrapper::class.java, DataWrapperType.more.name)
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     *  Builder. The client will be in charge with HTTP Requests. Besides sending and reading HTTP Req.,
     *  it also handles 3 other functions :
     *      -> [DefaultTokenInterceptor] Ensures that the authentication token is attached to every request header.
     *      -> [RedditAuthenticator] Ensures that unauthorized requests are retried using a new Reddit token.
     *      -> [DEBUG PURPOSE] [LoggingInterceptor] Ensures that every HTTP request is logged.
     *
     *      NOTE: The [loggingInterceptor] should not be attached in released applications.
     */
    private val httpClient = OkHttpClient.Builder()
        .authenticator(RedditAuthenticator(authenticationCall))
        .addInterceptor(DefaultTokenInterceptor(tokenManager))
        .addInterceptor(loggingInterceptor)

    /**
     *  Builder. The resulting client adapts the API interfaces to HTTP calls. It uses the moshi client
     *  to ensure data si properly (de)serialized and the RxJava2 adapter to allow API interface methods
     *  to return Rx Observers.
     */
    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshiClient))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    /**
    *   Retrofit client. The instance can be used for all calls to token based APIs.
    */
    private val defaultRetrofit = retrofitBuilder
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .build()

    /**
     *  Public method which returns an API service instance for a given Service class.
     */
    fun <S> createService(serviceClass: Class<S>): S {
        return defaultRetrofit.create(serviceClass)
    }

    /**
     *  A dedicated method for creating AuthenticationApi instances.
     *
     *  Note: Since the AuthenticationApi uses a separate authentication protocol, a different base url,
     *  it was decided to create separate retrofit and okHttp instances.
     */
    fun createAuthenticationService(): AuthenticationApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationServiceTokenInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        val authenticationRetrofit = retrofitBuilder
            .client(okHttpClient)
            .baseUrl(BASE_URL_PUBLIC)
            .build()

        return authenticationRetrofit.create(AuthenticationApi::class.java)
    }


}

