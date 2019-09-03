package com.sogard.domain.usecases

import com.sogard.domain.models.AuthenticationState
import com.sogard.domain.models.PaginationParameters
import com.sogard.domain.repositories.AuthenticationRepository
import com.sogard.domain.repositories.PostRepository
import extensions.applyIoScheduler
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class ApplicationInitializationUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val postRepository: PostRepository
) {

    /** Delay the result a bit to ensure the splash screen is not disappearing too fast.*/
    fun initializeApplication(): Completable =
        checkAuthentication().andThen(preFetchPosts()).applyIoScheduler().delay(
            500,
            TimeUnit.MILLISECONDS
        )

    private fun preFetchPosts(): Completable =
        Completable.fromSingle(postRepository.getTopPosts(PaginationParameters(null, 0, 15)))

    //TODO: Discuss whether we want to abstract the token availability/validity at the repository level.
    private fun authenticate(): Completable = authenticationRepository.fetchToken()

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