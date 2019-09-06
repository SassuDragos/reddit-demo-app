package com.sogard.data.network

import io.reactivex.Single
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * An OKHttp Authenticator that refreshes the OAuth token for those requests that fail due to
 * Unauthorized access (401) reasons.
 * */
class RedditAuthenticator(private val authenticationCall: () -> Single<String>) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Fetch a new token (This step includes saving it in the Storage and updating the app state.
        val newToken = authenticationCall().blockingGet()

        return if (newToken != null) {
            // Attempt request again
            response.request.newBuilder()
                .header("Authorization", newToken)
                .build()
        } else {
            response.request
        }
    }
}
