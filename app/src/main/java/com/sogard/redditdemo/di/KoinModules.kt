package com.sogard.redditdemo.di

import com.sogard.data.ApiServiceGenerator
import com.sogard.data.apis.AuthenticationApi
import com.sogard.data.apis.PostApi
import com.sogard.data.repositories.AuthenticationRepositoryImpl
import com.sogard.data.repositories.PostRepositoryImpl
import com.sogard.domain.repositories.AuthenticationRepository
import com.sogard.domain.repositories.PostRepository
import com.sogard.domain.usecases.ApplicationInitializationUseCase
import com.sogard.domain.usecases.TopPostsManagementUseCase
import datasources.SharedPreferencesHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val helperModule: Module = module {
    single { SharedPreferencesHelper(androidContext()) }
}
private val API_GENERATOR = "API_GENERATOR"

val networkModule: Module = module {
    single(named(API_GENERATOR)) { ApiServiceGenerator(get()) }
    single<PostApi> { get<ApiServiceGenerator>(named(API_GENERATOR)).createService(PostApi::class.java) }
    single<AuthenticationApi> {
        get<ApiServiceGenerator>(named(API_GENERATOR)).createAuthenticationService()
    }
}

val repositoryModule: Module = module {
    single<PostRepository> { PostRepositoryImpl(postApi = get()) }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            sharedPrefHelper = get(),
            authenticationApi = get()
        )
    }
}

val useCaseModule: Module = module {
    single {
        ApplicationInitializationUseCase(authenticationRepository = get(), postRepository = get())
    }
    single { TopPostsManagementUseCase(postRepository = get()) }
}

