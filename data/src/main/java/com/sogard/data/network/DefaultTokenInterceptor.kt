package com.sogard.data.network

import com.sogard.data.apis.AppConfiguration
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

const val UNAUTHENTICATED_TOKEN = "Bearer "

abstract class BaseAuthenticationInterceptor : Interceptor {
    abstract var activeToken: String

    companion object {
        var test = 0
    }


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = if (test == 3) activeToken + 55 else activeToken
        val builder = original.newBuilder()
            .header("Authorization", token)
        test++
        val request = builder.build()
        return chain.proceed(request)
    }
}

/**
 * The interceptor provides the Basic Auth token required to do Authorization requests.
 * */
class AuthorizationServiceTokenInterceptor : BaseAuthenticationInterceptor() {
    override var activeToken: String = AppConfiguration.BASIC_AUTH_APP_TOKEN
}

class DefaultTokenInterceptor(tokenManager: TokenManager) :
    BaseAuthenticationInterceptor() {

    override var activeToken: String =
        tokenManager.tokenSubject.value?.formattedToken ?: UNAUTHENTICATED_TOKEN

    init {
        val subscription = tokenManager.tokenSubject
            .map { it.formattedToken ?: UNAUTHENTICATED_TOKEN }
            .subscribe { activeToken = it }
    }
}