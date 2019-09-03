package com.sogard.data.apis

import com.sogard.data.apis.AppConfiguration.REDDIT_PUBLIC_BASE_URL
import com.sogard.data.apis.AppConfiguration.BASE_URL
import com.sogard.data.datasources.SharedPrefKeys
import com.sogard.data.datasources.SharedPreferencesHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.logging.HttpLoggingInterceptor



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

    private val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY}
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthenticationInterceptor {
            //TODO: This is sub optimal. The token should be cached and the cache component should
            // be provided instead.
            sharedPrefHelper.getString(SharedPrefKeys.KEY_TOKEN) ?: ""
        })
        .addInterceptor(loggingInterceptor);

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
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

