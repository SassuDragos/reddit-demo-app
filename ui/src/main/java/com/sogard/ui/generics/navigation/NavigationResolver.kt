package com.sogard.ui.generics.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sogard.ui.helpers.NavigationAction
import com.sogard.ui.helpers.NavigationHandler

open class NavigationResolver(private val activity: AppCompatActivity) :
    Observer<NavigationAction> {
    override fun onChanged(action: NavigationAction?) {
        action?.let { NavigationHandler.goTo(action, activity) }
    }
}
