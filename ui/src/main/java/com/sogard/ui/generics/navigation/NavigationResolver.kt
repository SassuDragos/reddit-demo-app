package com.sogard.ui.generics.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sogard.ui.helpers.NavigationDestination
import com.sogard.ui.helpers.NavigationHandler

open class NavigationResolver(private val activity: AppCompatActivity) :
    Observer<NavigationDestination> {
    override fun onChanged(destination: NavigationDestination?) {
        destination?.let { NavigationHandler.goTo(destination, activity) }
    }
}
