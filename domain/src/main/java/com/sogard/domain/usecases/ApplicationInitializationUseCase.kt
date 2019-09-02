package com.sogard.domain.usecases

import com.sogard.domain.models.AuthenticationState
import com.sogard.domain.repositories.AuthenticationRepository
import com.sogard.domain.repositories.PostRepository
import extensions.applyIoScheduler
import io.reactivex.Completable

class ApplicationInitializationUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val postRepository: PostRepository
) {

    fun initializeApplication(): Completable =
        checkAuthentication().andThen(fetchPosts()).applyIoScheduler()


    private fun fetchPosts(): Completable =
        Completable.fromSingle(postRepository.getTopPosts())


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