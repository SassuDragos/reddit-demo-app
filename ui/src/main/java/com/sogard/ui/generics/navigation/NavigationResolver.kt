package com.sogard.ui.generics.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

/**
 * An observer which handles NavigationActions emitted by an Observable Source. It should be used in
 * an context aware environment, to abstract the calls to the NavigationHandler.
 * */
open class NavigationResolver(private val activity: AppCompatActivity) :
    Observer<NavigationAction> {
    override fun onChanged(action: NavigationAction?) {
        action?.let { NavigationHandler.goTo(action, activity) }
    }
}
