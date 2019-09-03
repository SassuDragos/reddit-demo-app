package com.sogard.data.datasources

import android.content.Context
import com.sogard.data.repositories.SharedPrefKeys.DEFAULT_FILE_NAME


//TODO: For the current scope, shared preferences is secure / good enough as a storage facility.
// However, for further development, a more solid storage facility should be used (e.g. room/realm),
// possibly in combination with an encryption library for storing access tokens.

class SharedPreferencesHelper(applicationContext: Context) {
    private val sharedPreferences =
        applicationContext.getSharedPreferences(DEFAULT_FILE_NAME, Context.MODE_PRIVATE)
    private val sharedPreferencesEditor = sharedPreferences.edit()

    fun putString(key: String, value: String) {
        sharedPreferencesEditor.putString(key, value)
        sharedPreferencesEditor.apply()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putLong(key: String, value: Long) {
        sharedPreferencesEditor.putLong(key, value)
        sharedPreferencesEditor.apply()
    }

    fun getLong(key: String): Long? {
        return if (sharedPreferences.contains(key)) {
            sharedPreferences.getLong(key, 0)
        } else null
    }
}
