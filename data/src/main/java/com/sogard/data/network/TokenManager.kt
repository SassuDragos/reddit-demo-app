package com.sogard.data.network

import com.sogard.data.datasources.SharedPrefKeys
import com.sogard.data.datasources.SharedPreferencesHelper
import com.sogard.data.repositories.TokenData
import io.reactivex.subjects.BehaviorSubject

/**
 *
 * Note: Naming could be improved.
 */
class TokenManager(sharedPrefHelper: SharedPreferencesHelper) {

    val tokenSubject: BehaviorSubject<TokenData> by lazy {
        val formattedToken = sharedPrefHelper.getString(SharedPrefKeys.KEY_TOKEN)

        BehaviorSubject.create<TokenData>().apply { onNext(TokenData(formattedToken)) }
    }
}