package com.sogard.domain.usecases

import com.sogard.domain.repositories.AuthenticationRepository
import com.sogard.domain.repositories.PostRepository
import extensions.applyIoScheduler
import io.reactivex.Completable

class ApplicationInitializationUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val postRepository: PostRepository
) {

    fun initializeApplication(): Completable {
        return authenticate().andThen(fetchPosts()).applyIoScheduler()
    }


    private fun fetchPosts(): Completable {
        return Completable.fromSingle(postRepository.getTopPosts())
    }

    //TODO: Discuss whether we want to abstract the token availability/validity at the repository level.
    private fun authenticate(): Completable {
        return authenticationRepository.isTokenAvailable()
            .flatMapCompletable { isAvailable ->
                if (isAvailable) {
                    authenticationRepository.isTokenValid()
                        .flatMapCompletable { isValid ->
                            if (isValid) {
                                Completable.complete()
                            } else {
                                authenticationRepository.refreshToken()
                            }
                        }
                } else {
                    authenticationRepository.fetchToken()
                }
            }
    }


}