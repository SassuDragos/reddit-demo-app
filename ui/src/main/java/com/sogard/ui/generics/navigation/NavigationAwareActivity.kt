package com.sogard.ui.generics.navigation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sogard.ui.generics.BaseViewModel
import com.sogard.ui.helpers.NavigationAction

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
    abstract fun getNavigationResolver(): Observer<NavigationAction>

    var viewModel: T? = null
        set(value) {
            value?.navigationLiveData?.observe(this, getNavigationResolver())
            field = value
        }

    abstract fun createViewModel(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = createViewModel()
    }
}

