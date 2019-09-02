package com.sogard.redditdemo

import android.app.Application
import com.sogard.redditdemo.di.helperModule
import com.sogard.redditdemo.di.networkModule
import com.sogard.redditdemo.di.repositoryModule
import com.sogard.redditdemo.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {



    override fun onCreate() {
        super.onCreate()

        //TODO: Start Koin here
        startKoin {
            // Android context
            androidContext(this@MainApplication)
            // modules
            modules(listOf(helperModule, networkModule, repositoryModule, useCaseModule))
        }
    }
}