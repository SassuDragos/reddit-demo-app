package com.sogard.ui.generics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sogard.ui.generics.navigation.NavigationAction
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

/**
 * A viewModel abstraction which contains the core viewModel functions that should be available
 * in all lifecycle aware viewModels:
 *  -> Extends the Android Lifecycle aware viewModel
 *  -> Exposes a LiveData instance which can be subscribed to get navigation events.
 *  -> Exposes a LiveData instance which can be subscribed to get UIState updates.
 *  -> Defines a CompositeDisposable that will hold any viewModel Rx subscriptions.
 *  -> Cleared the Disposable when needed.
 * */
abstract class BaseViewModel : ViewModel(), KoinComponent {

    val currentState: MutableLiveData<UIState> = MutableLiveData()

    protected val subscriptions: CompositeDisposable = CompositeDisposable()

    //TODO: Alternatively, we can (1) put the click listeners in the viewModel and use the view
    //      to obtain the context or (2) we can create a generic subscription mechanism for every
    //      Fragment/Activity which is attached to a BaseViewModel
    val navigationLiveData = MutableLiveData<NavigationAction>()

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}