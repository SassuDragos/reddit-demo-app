package com.sogard.ui.features.splash

import android.util.Log
import com.sogard.domain.usecases.ApplicationInitializationUseCase
import com.sogard.ui.features.splash.LoggerTags.SUBSCRIPTION_ERROR
import com.sogard.ui.features.splash.LoggerTags.SUBSCRIPTION_SUCCESS
import com.sogard.ui.generics.BaseViewModel
import com.sogard.ui.generics.UIState
import com.sogard.ui.generics.navigation.NavigationAction
import org.koin.core.inject

object LoggerTags {
    const val SUBSCRIPTION_SUCCESS = "SUBSCRIPTION SUCCESS: "
    const val SUBSCRIPTION_ERROR = "SUBSCRIPTION ERROR: "
}

open class SplashViewModel : BaseViewModel() {

    private val appInitUseCase: ApplicationInitializationUseCase by inject()

    fun initializeAppData() {
        val subscription = appInitUseCase.initializeApplication()
            .doOnSubscribe { }
            .subscribe({
                Log.i(SUBSCRIPTION_SUCCESS, "APP Successfully Initialized")
                navigationLiveData.postValue(NavigationAction.ArticleListAction(false))
                currentState.postValue(UIState.Success)
            }, {
                Log.e(SUBSCRIPTION_ERROR, it.message)
                currentState.postValue(UIState.Error)
            })
        subscriptions.add(subscription)
    }
}
