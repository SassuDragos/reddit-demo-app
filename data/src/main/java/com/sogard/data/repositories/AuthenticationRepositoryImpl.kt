package com.sogard.data.repositories

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.sogard.data.apis.AuthenticationApi
import com.sogard.data.datasources.SharedPrefKeys.KEY_DEVICE_ID
import com.sogard.data.datasources.SharedPrefKeys.KEY_TOKEN
import com.sogard.data.datasources.SharedPreferencesHelper
import com.sogard.data.models.AccessTokenDAO
import com.sogard.data.network.AppNetworkConfiguration.GRANT_TYPE_DEFAULT
import com.sogard.data.network.TokenData
import com.sogard.data.network.TokenManager
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

class AuthenticationRepositoryImpl(
    private val sharedPrefHelper: SharedPreferencesHelper,
    private val tokenManager: TokenManager,
    private val applicationContext: Context,
    private val authenticationApi: AuthenticationApi
) : AuthenticationRepository {

    private val deviceId: String =
        sharedPrefHelper.getString(KEY_DEVICE_ID) ?: createAndStoreNewDeviceId()

    /**
     * Property subscribing/emitting the latest AuthenticationState.
     * */
    private val authStateSubject: BehaviorSubject<AuthenticationState> by lazy {
        val authenticatedState = tokenManager.tokenSubject.value?.toAuthState() ?: NotAuthenticated

        BehaviorSubject.create<AuthenticationState>().apply { onNext(authenticatedState) }
    }

    init {
        //  Allow the authStateSubject to be notified about token related changes. They can trigger
        //  changes in the authentication state.
        tokenManager.tokenSubject.mapToAuthState().subscribe(authStateSubject)
    }

    /**
     * Method that emits the latest authentication state.
     * */
    override fun getAuthenticationState(): Single<AuthenticationState> {
        return Single.just(authStateSubject.value)
    }

    /**
     *  Method that fetches a new server token, saves in the "storage", publishes it to the token
     *  manager and returns a SingleSource for it.
     */
    //TODO: Replace the String return type with a TokenData instance.
    override fun getToken(): Single<String> =
        authenticationApi.getAccessToken(GRANT_TYPE_DEFAULT, deviceId)
            .doOnSuccess(::saveTokenDAOToLocalStorage)
            .map { TokenData(it.toFormattedToken()) }
            .doOnSuccess { tokenManager.tokenSubject.onNext(it) }
            .doOnError { tokenManager.tokenSubject.onNext(TokenData(null)) }
            .map { it.formattedToken }

    /** Method that hides the token result. For consumers who are only interested of the success state. */
    override fun fetchToken(): Completable = Completable.fromSingle(getToken())

    /**
     *  [Trial method] Added this functionality to see how WorkManager works. NOT TESTED!
     *
     *  Method that starts the [RefreshTokenWorker] and schedules it every 40 min. (token expires every 60 min.).
     *
     *  Note: This feature is not a must, as the issue is solved by the RedditAuthenticator attached
     *  to the okHttpclient.
     * */
    override fun startTokenRefreshingWorker() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val refreshTokenWorker =
            PeriodicWorkRequest.Builder(RefreshTokenWorker::class.java, 40, TimeUnit.MINUTES)
                .addTag(RefreshTokenWorker.TAG)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueue(refreshTokenWorker)
    }

    /**
     * Method that takes an [AccessTokenDAO] and saves the data in SharedPref. for later usage.*/
    private fun saveTokenDAOToLocalStorage(accessTokenDAO: AccessTokenDAO) {
        sharedPrefHelper.putString(KEY_DEVICE_ID, accessTokenDAO.deviceId)
        sharedPrefHelper.putString(KEY_TOKEN, accessTokenDAO.toFormattedToken())
    }

    private fun createAndStoreNewDeviceId(): String {
        val newId = UUID.randomUUID().toString()
        sharedPrefHelper.putString(KEY_DEVICE_ID, newId)
        return newId
    }

    /** Method that formats the auth. token to the exact use form.*/
    private fun AccessTokenDAO.toFormattedToken(): String {
        Log.i("TOKEN", "$tokenType $token")
        return "$tokenType $token"
    }

    private fun TokenData.toAuthState(): AuthenticationState =
        if (formattedToken != null) Authenticated else NotAuthenticated

    private fun io.reactivex.Observable<TokenData>.mapToAuthState(): io.reactivex.Observable<AuthenticationState> =
        this.map { it.toAuthState() }
}