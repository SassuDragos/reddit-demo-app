package com.sogard.ui.helpers

import android.content.Context

class ResourceProvider (val applicationContext: Context) {


    fun getString(stringIdentifier: Int) = applicationContext.resources.getString(stringIdentifier)
}