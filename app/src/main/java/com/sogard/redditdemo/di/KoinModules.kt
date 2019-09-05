package com.sogard.redditdemo.di

import com.sogard.data.apis.ApiServiceGenerator
import com.sogard.data.apis.ArticleApi
import com.sogard.data.apis.CommentsApi
import com.sogard.data.datasources.SharedPreferencesHelper
import com.sogard.data.repositories.ArticleRepositoryImpl
import com.sogard.data.repositories.AuthenticationRepositoryImpl
import com.sogard.data.repositories.CommentRepositoryImpl
import com.sogard.data.repositories.TokenManager
import com.sogard.domain.repositories.ArticleRepository
import com.sogard.domain.repositories.AuthenticationRepository
import com.sogard.domain.repositories.CommentRepository
import com.sogard.domain.usecases.ApplicationInitializationUseCase
import com.sogard.domain.usecases.CommentListingUseCase
import com.sogard.domain.usecases.TopArticleListingUseCase
import com.sogard.ui.helpers.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


val helperModule: Module = module {
    single { SharedPreferencesHelper(androidContext()) }
    single { ResourceProvider(androidContext()) }
}

val networkModule: Module = module {
    single { TokenManager(get()) }
    single {
        ApiServiceGenerator(
            authenticationCall = { get<AuthenticationRepository>().getToken() },
            tokenManager = get()
        )
    }
    single { get<ApiServiceGenerator>().createService(ArticleApi::class.java) }
    single { get<ApiServiceGenerator>().createAuthenticationService() }
    single { get<ApiServiceGenerator>().createService(CommentsApi::class.java) }
}

val repositoryModule: Module = module {
    single<CommentRepository> { CommentRepositoryImpl(commentsApi = get()) }
    single<ArticleRepository> { ArticleRepositoryImpl(articleApi = get()) }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            sharedPrefHelper = get(),
            authenticationApi = get(),
            applicationContext = get(),
            tokenManager = get()
        )
    }
}

//Note that in a larger pr
val useCaseModule: Module = module {
    single {
        ApplicationInitializationUseCase(
            authenticationRepository = get(),
            articleRepository = get()
        )
    }
    factory { TopArticleListingUseCase(articleRepository = get()) }
    factory { CommentListingUseCase(commentRepository = get()) }
}

