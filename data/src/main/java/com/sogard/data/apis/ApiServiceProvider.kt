package com.sogard.data.apis

import com.sogard.data.apis.AppConfiguration.BASE_URL
import com.sogard.data.apis.AppConfiguration.REDDIT_PUBLIC_BASE_URL
import com.sogard.data.datasources.SharedPrefKeys
import com.sogard.data.datasources.SharedPreferencesHelper
import com.sogard.data.models.*
import com.squareup.moshi.*
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.JsonAdapter




object AppConfiguration {
    //TODO: These could be defined in build/flavor config.
    val BASE_URL: String = "https://oauth.reddit.com"
    val REDDIT_PUBLIC_BASE_URL = "https://www.reddit.com"

    val DEFAULT_GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"

    //TODO: This token should be encrypted and saved in a local storage. It can be easily hacked with reverse
    // engineering techniques. For the scope of this app (at least at this point), security is not central.
    val BASIC_AUTH_APP_TOKEN = "Basic YjVDYTNRVDRfWHh0ZlE6"
}

//TODO: Make this class singleton
class ApiServiceGenerator(sharedPrefHelper: SharedPreferencesHelper) {

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val moshiClient = Moshi.Builder()
        .add(PolymorphicJsonAdapterFactory.of(CoreRedditDAO::class.java, KEY_WRAPPER_TYPE)
            .withSubtype(ListingData::class.java, DataWrapperType.Listing.name)
            .withSubtype(ArticleDAO::class.java, DataWrapperType.t3.name)
            .withSubtype(CommentDAO::class.java, DataWrapperType.t1.name)
            .withSubtype(MoreDataDAO::class.java, DataWrapperType.more.name))
        .add(
            PolymorphicJsonAdapterFactory.of(DataWrapper::class.java, KEY_WRAPPER_TYPE)
                .withSubtype(ListingWrapper::class.java, DataWrapperType.Listing.name)
                .withSubtype(ArticleWrapper::class.java, DataWrapperType.t3.name)
                .withSubtype(CommentWrapper::class.java, DataWrapperType.t1.name)
                .withSubtype(MoreDataWrapper::class.java, DataWrapperType.more.name)
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthenticationInterceptor {
            //TODO: This is sub optimal. The token should be cached and the cache component should
            // be provided instead.
            sharedPrefHelper.getString(SharedPrefKeys.KEY_TOKEN) ?: ""
        })
        .addInterceptor(loggingInterceptor)

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshiClient))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private val defaultRetrofit = retrofitBuilder
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .build()

    fun createAuthenticationService(): AuthenticationApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor { AppConfiguration.BASIC_AUTH_APP_TOKEN })
            .addInterceptor(loggingInterceptor)
            .build()

        val authenticationRetrofit = retrofitBuilder
            .baseUrl(REDDIT_PUBLIC_BASE_URL)
            .client(okHttpClient)
            .build()

        return authenticationRetrofit.create(AuthenticationApi::class.java)
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return defaultRetrofit.create(serviceClass)
    }
}

