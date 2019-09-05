package com.sogard.domain.repositories

import com.sogard.domain.models.authentication.AuthenticationState
import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticationRepository {

    fun fetchToken(): Completable

    fun getAuthenticationState(): Single<AuthenticationState>

    fun startTokenRefreshingWorker()
}