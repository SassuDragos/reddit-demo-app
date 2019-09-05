package com.sogard.domain.repositories

import com.sogard.domain.models.authentication.AuthenticationState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface AuthenticationRepository {

    fun getToken(): Single<String>

    fun fetchToken(): Completable

    fun getAuthenticationState(): Single<AuthenticationState>

    fun startTokenRefreshingWorker()
}