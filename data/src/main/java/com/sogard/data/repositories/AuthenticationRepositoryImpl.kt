package com.sogard.data.repositories

import android.content.Context
import com.sogard.data.AppConfiguration.DEFAULT_GRANT_TYPE
import com.sogard.data.apis.AuthenticationApi
import com.sogard.data.models.AccessTokenDAO
import com.sogard.data.repositories.SharedPrefKeys.KEY_DEVICE_ID
import com.sogard.data.repositories.SharedPrefKeys.KEY_TOKEN
import com.sogard.data.repositories.SharedPrefKeys.KEY_TOKEN_EXPIRATION_TIME
import com.sogard.domain.models.AuthenticationState
import com.sogard.domain.repositories.AuthenticationRepository
import datasources.SharedPreferencesHelper
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*


object SharedPrefKeys {
    const val DEFAULT_FILE_NAME = "mock_reddit_data"
    const val KEY_TOKEN = "KEY_TOKEN"
    const val KEY_TOKEN_EXPIRATION_TIME = "KEY_TOKEN_EXPIRATION_TIME"
    const val KEY_DEVICE_ID = "KEY_DEVICE_ID"
}

class AuthenticationRepositoryImpl(
    val sharedPrefHelper: SharedPreferencesHelper,
    val authenticationApi: AuthenticationApi
) : AuthenticationRepository {

    private val authStateSubject: BehaviorSubject<AuthenticationState> = BehaviorSubject.create()

    override fun getAuthenticationState(): Single<AuthenticationState> {
        if (!authStateSubject.hasValue()) {
            val token = sharedPrefHelper.getString(KEY_TOKEN)
            val tokenExpirationTime = sharedPrefHelper.getLong(KEY_TOKEN_EXPIRATION_TIME)

            val authenticatedState = createAuthState(token, tokenExpirationTime)

            authStateSubject.onNext(authenticatedState)
        }
        return Single.just(authStateSubject.value)
    }

    private val deviceId: String by lazy {
        sharedPrefHelper.getString(KEY_DEVICE_ID) ?: createNewDeviceId()
    }

    override fun fetchToken(): Completable {
        return Completable.fromSingle(
            authenticationApi.getAccessToken(DEFAULT_GRANT_TYPE, deviceId)
                .doOnSuccess {
                    saveTokenData(it)
                    val newState =
                        createAuthState(it.token, getExpirationTime(it.secondsToExpiration))
                    authStateSubject.onNext(newState)
                }
        )
    }

    /**
     * Method that takes an [AccessTokenDAO] and saves the data in SharedPref. for later usage.
     * */
    private fun saveTokenData(accessTokenDAO: AccessTokenDAO) {
        sharedPrefHelper.putString(KEY_TOKEN, accessTokenDAO.tokenType + " " + accessTokenDAO.token)
        sharedPrefHelper.putString(KEY_DEVICE_ID, accessTokenDAO.deviceId)

        val tokenExpirationTime: Long = getExpirationTime(accessTokenDAO.secondsToExpiration)
        sharedPrefHelper.putLong(KEY_TOKEN_EXPIRATION_TIME, tokenExpirationTime)

        authStateSubject.onNext(createAuthState(accessTokenDAO.token, tokenExpirationTime))
    }

    /**
     * Based on a given [token] and a given [tokenExpirationTime], the method returns whether the application
     * is currently authenticated or not.
     * */
    private fun createAuthState(token: String?, expirationTime: Long?): AuthenticationState {
        val isTokenValid = if (expirationTime != null) !isTokenExpired(expirationTime) else false

        return if (token != null && isTokenValid) {
            AuthenticationState.Authenticated(token, expirationTime!!)
        } else {
            AuthenticationState.NotAuthenticated
        }
    }

    private fun getExpirationTime(secondsToExpiration: Int): Long {
        return Date().time + secondsToExpiration * 1000
    }

    private fun isTokenExpired(expirationTime: Long): Boolean {
        return Date(expirationTime) < Date()
    }

    private fun createNewDeviceId(): String {
        val newId = UUID.randomUUID().toString()
        sharedPrefHelper.putString(KEY_DEVICE_ID, newId)
        return newId
    }
}