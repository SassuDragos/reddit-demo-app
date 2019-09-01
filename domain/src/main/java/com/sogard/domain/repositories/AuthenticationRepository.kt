package com.sogard.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticationRepository {

    fun fetchToken(): Completable

    fun refreshToken(): Completable

    fun isTokenValid(): Single<Boolean>

    fun isTokenAvailable(): Single<Boolean>
}