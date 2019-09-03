package com.sogard.data.apis

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor(private val getAuthorizationHeader: () -> String) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
            .header("Authorization", getAuthorizationHeader())

        val request = builder.build()
        return chain.proceed(request)
    }
}