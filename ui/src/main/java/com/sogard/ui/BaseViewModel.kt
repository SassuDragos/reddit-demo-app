package com.sogard.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

abstract class BaseViewModel() : ViewModel(), KoinComponent {
    protected  val subscriptions: CompositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}