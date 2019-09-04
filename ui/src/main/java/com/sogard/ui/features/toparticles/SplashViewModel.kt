package com.sogard.ui.features.toparticles

import android.util.Log
import com.sogard.domain.usecases.ApplicationInitializationUseCase
import com.sogard.ui.generics.BaseViewModel
import org.koin.core.inject

open class SplashViewModel : BaseViewModel() {
    private val appInitUseCase: ApplicationInitializationUseCase by inject()

    init {
        val subscription = appInitUseCase
            .initializeApplication()
            .subscribe({
                Log.i("[SUCCESS]", "APP Successfully Initialized")
            }, {
                Log.e("[APP INIT FAILED] ", it.message)
            })
        subscriptions.add(subscription)
    }
}
