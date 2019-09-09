package com.sogard.domain.usecases

import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.authentication.AuthenticationState
import com.sogard.domain.repositories.ArticleRepository
import com.sogard.domain.repositories.AuthenticationRepository
import extensions.applyIoScheduler
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class ApplicationInitializationUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val articleRepository: ArticleRepository
) {

    /** Delay the result a bit to ensure the splash screen is not disappearing too fast.*/
    fun initializeApplication(): Completable =
        checkAuthentication()
            .andThen(preFetchPosts())
            .applyIoScheduler()
            .delay(500, TimeUnit.MILLISECONDS)
            .doOnComplete { authenticationRepository.startTokenRefreshingWorker() }

    //TODO: There is no cashing mechanism yet. This call will serve for future use.
    private fun preFetchPosts(): Completable =
        Completable.fromSingle(
            articleRepository.getTopArticles(
                ArticlePaginationParameters(
                    null,
                    0,
                    15
                )
            )
        )

    private fun authenticate(): Completable = authenticationRepository.fetchToken()

    //TODO: Discuss whether we want to abstract the token availability/validity at the repository level.
    private fun checkAuthentication(): Completable =
        authenticationRepository.getAuthenticationState()
            .flatMapCompletable { state ->
                if (state is AuthenticationState.Authenticated) {
                    Completable.complete()
                } else {
                    authenticate()
                }
            }


}