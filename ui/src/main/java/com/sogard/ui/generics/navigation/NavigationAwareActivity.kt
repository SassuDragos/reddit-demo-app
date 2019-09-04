package com.sogard.ui.generics.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sogard.ui.generics.BaseViewModel
import com.sogard.ui.helpers.NavigationDestination
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Base activity which connects the live
 * */
abstract class NavigationAwareActivity<T : BaseViewModel> : AppCompatActivity() {

    /**
     *  The method allows subclasses to resolve navigation requests in their own way.
     *  The [NavigationResolver] implements a basic routing handling.
     *
     *  Note:   The existing navigation use cases only require simple calls to NavigationHandler.goTo().
     *          However, this method allows for Activity specific navigation behavior (e.g. popup dialog).
     *          Alternatively, the navigation resolver could be placed directly in the [viewModel] setter
     *          but that comes at the cost of losing implementation flexibility.
     */
    abstract fun getNavigationResolver(): Observer<NavigationDestination>

    var viewModel: T? = null
        set(value) {
            value?.navigationLiveData?.observe(this, getNavigationResolver())
            field = value
        }
}

