package com.sogard.data.apis

import com.sogard.data.repositories.TokenManager
import com.sogard.domain.models.authentication.AuthenticationState
import com.sogard.domain.repositories.AuthenticationRepository
import extensions.applyIoScheduler
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.internal.closeQuietly
import java.util.concurrent.TimeUnit
import javax.security.auth.Subject

/**
 * An OKHttp Authenticator that refreshes the OAuth token for those requests that fail due to
 * Unauthorized access reasons.
 * */
class RedditAuthenticator(private val authenticationCall: () -> Single<String>,
                          private val tokenManager: TokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Fetch a new token (This step includes saving it in the Storage and uploading the app state.
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
