package com.sogard.domain.repositories

import com.sogard.domain.models.authentication.AuthenticationState
import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticationRepository {

    /** Trigger a call to the Authentication service to get a new API token and then return it.*/
    fun getToken(): Single<String>

    /** Trigger a call to the Authentication service to get a new API token. Will notify
     * the subscriber when the token has been loaded.*/
    fun fetchToken(): Completable

    /** Get the current Authentication state of the application.*/
    fun getAuthenticationState(): Single<AuthenticationState>

    /** [EXPERIMENTAL] Start a worker that periodically fetches a new application token. */
    fun startTokenRefreshingWorker()
}