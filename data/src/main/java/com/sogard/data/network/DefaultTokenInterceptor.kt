package com.sogard.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal const val UNAUTHENTICATED_TOKEN = "Bearer "

/**
 *  Abstract Interceptor class in charge with adding an [activeToken] authorization parameter to
 *  every intercepted request.
 */
abstract class BaseAuthenticationInterceptor : Interceptor {
    abstract var activeToken: String

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
            .header("Authorization", activeToken)

        return chain.proceed(builder.build())
    }
}

/**
 *  A subclass of BaseAuthenticationInterceptor configured with the Basic Auth Token.
 */
class AuthorizationServiceTokenInterceptor : BaseAuthenticationInterceptor() {
    override var activeToken: String = AppNetworkConfiguration.BASIC_AUTH_APP_TOKEN
}

/**
 *  A subclass of BaseAuthenticationInterceptor which ensures that every request has the latest available
 *  token.
 *
 *  [tokenManager]:  Token provider.
 * */
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