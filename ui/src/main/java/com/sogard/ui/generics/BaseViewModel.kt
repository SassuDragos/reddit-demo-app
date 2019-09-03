package com.sogard.ui.generics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sogard.ui.helpers.NavigationDestination
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

abstract class BaseViewModel : ViewModel(), KoinComponent {
    protected val subscriptions: CompositeDisposable = CompositeDisposable()


    //TODO: Alternatively, we can (1) put the click listeners in the viewModel and use the view
    //      to obtain the context or (2) we can create a generic subscription mechanism for every
    //      Fragment/Activity which is attached to a BaseViewModel
    val navigationLiveData = MutableLiveData<NavigationDestination>()

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}