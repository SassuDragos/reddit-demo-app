package com.sogard.ui.helpers

import android.content.Context

class ResourceProvider (private val applicationContext: Context) {

    fun getString(stringIdentifier: Int): String = applicationContext.resources.getString(stringIdentifier)
}