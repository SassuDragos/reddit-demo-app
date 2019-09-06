package com.sogard.data.network

import com.sogard.data.datasources.SharedPrefKeys
import com.sogard.data.datasources.SharedPreferencesHelper
import io.reactivex.subjects.BehaviorSubject

/**
 * Data class used for wrapping the local version of the token. This helps avoiding working
 * with null token Strings.
 * */
data class TokenData(val formattedToken: String?)

/**
 *  Class which handles the authentication token propagation across all app components
 *  that depend on it.
 *
 *  Note: Naming could be improved.
 *
 *  [sharedPrefHelper]: A interface for the SharedPreferences Android class.
 */
class TokenManager(sharedPrefHelper: SharedPreferencesHelper) {

    /**
     * Subject used for token propagation.
     * */
    val tokenSubject: BehaviorSubject<TokenData> by lazy {
        val formattedToken = sharedPrefHelper.getString(SharedPrefKeys.KEY_TOKEN)

        BehaviorSubject.create<TokenData>().apply { onNext(TokenData(formattedToken)) }
    }
}