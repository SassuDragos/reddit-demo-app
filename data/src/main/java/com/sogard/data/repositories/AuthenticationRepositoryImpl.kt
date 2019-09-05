package com.sogard.data.repositories

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.sogard.data.apis.AppConfiguration.DEFAULT_GRANT_TYPE
import com.sogard.data.apis.AuthenticationApi
import com.sogard.data.datasources.SharedPrefKeys.KEY_DEVICE_ID
import com.sogard.data.datasources.SharedPrefKeys.KEY_TOKEN
import com.sogard.data.datasources.SharedPreferencesHelper
import com.sogard.data.models.AccessTokenDAO
import com.sogard.data.workers.RefreshTokenWorker
import com.sogard.domain.models.authentication.AuthenticationState
import com.sogard.domain.models.authentication.AuthenticationState.Authenticated
import com.sogard.domain.models.authentication.AuthenticationState.NotAuthenticated
import com.sogard.domain.repositories.AuthenticationRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit


data class TokenData(val formattedToken: String?)

class TokenManager(sharedPrefHelper: SharedPreferencesHelper) {

    val tokenSubject: BehaviorSubject<TokenData> by lazy {
        val formattedToken = sharedPrefHelper.getString(KEY_TOKEN)

        BehaviorSubject.create<TokenData>().apply { onNext(TokenData(formattedToken)) }
    }
}


class AuthenticationRepositoryImpl(
    private val sharedPrefHelper: SharedPreferencesHelper,
    private val tokenManager: TokenManager,
    private val applicationContext: Context,
    private val authenticationApi: AuthenticationApi
) : AuthenticationRepository {


    private val deviceId: String =
        sharedPrefHelper.getString(KEY_DEVICE_ID) ?: createAndStoreNewDeviceId()

    private val authStateSubject: BehaviorSubject<AuthenticationState> by lazy {
        val authenticatedState = tokenManager.tokenSubject.value?.toAuthState() ?: NotAuthenticated

        BehaviorSubject.create<AuthenticationState>().apply { onNext(authenticatedState) }
    }

    init {
        tokenManager.tokenSubject.mapToAuthState().subscribe(authStateSubject)
    }

    override fun getAuthenticationState(): Single<AuthenticationState> {
        return Single.just(authStateSubject.value)
    }

    override fun getToken(): Single<String> =
        authenticationApi.getAccessToken(DEFAULT_GRANT_TYPE, deviceId)
            .doOnSuccess(::saveTokenDAOToLocalStorage)
            .map { TokenData(it.toFormattedToken()) }
            .doOnSuccess { tokenManager.tokenSubject.onNext(it) }
            .doOnError { tokenManager.tokenSubject.onNext(TokenData(null)) }
            .map { it.formattedToken }

    override fun fetchToken(): Completable = Completable.fromSingle(getToken())

    override fun startTokenRefreshingWorker() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val refreshTokenWorker =
            PeriodicWorkRequest.Builder(RefreshTokenWorker::class.java, 15, TimeUnit.SECONDS)
                .addTag(RefreshTokenWorker.TAG)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueue(refreshTokenWorker)
    }

    /**
     * Method that takes an [AccessTokenDAO] and saves the data in SharedPref. for later usage.
     * */

    private fun saveTokenDAOToLocalStorage(accessTokenDAO: AccessTokenDAO) {
        sharedPrefHelper.putString(KEY_DEVICE_ID, accessTokenDAO.deviceId)
        sharedPrefHelper.putString(KEY_TOKEN, accessTokenDAO.toFormattedToken())
    }

    private fun createAndStoreNewDeviceId(): String {
        val newId = UUID.randomUUID().toString()
        sharedPrefHelper.putString(KEY_DEVICE_ID, newId)
        return newId
    }

    private fun AccessTokenDAO.toFormattedToken(): String {
        Log.i("TOKEN", "$tokenType $token")
        return "$tokenType $token"
    }

    private fun TokenData.toAuthState(): AuthenticationState =
        if (formattedToken != null) Authenticated else NotAuthenticated

    private fun io.reactivex.Observable<TokenData>.mapToAuthState(): io.reactivex.Observable<AuthenticationState> =
        this.map { it.toAuthState() }
}