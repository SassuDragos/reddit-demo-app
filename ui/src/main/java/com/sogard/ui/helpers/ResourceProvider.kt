package com.sogard.ui.helpers

import android.content.Context

/** Class that is in charge with resolving Strings, Drawables and Colors.*/

class ResourceProvider (private val applicationContext: Context) {

    fun getString(stringIdentifier: Int): String = applicationContext.resources.getString(stringIdentifier)
}