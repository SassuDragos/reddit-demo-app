package com.sogard.domain.usecases

import com.sogard.domain.models.PaginationParameters
import com.sogard.domain.models.Post
import com.sogard.domain.repositories.PostRepository
import extensions.applyIoScheduler
import io.reactivex.Single

class TopPostsManagementUseCase(private val postRepository: PostRepository) {

    private var nextAnchor: String? = null
    private val MAX_RESPONSE_SIZE = 15

    fun getPosts(totalLoadedItems: Int): Single<List<Post>> {
        if (totalLoadedItems == 0) nextAnchor = null

        val paginationParameters =
            PaginationParameters(nextAnchor, totalLoadedItems, MAX_RESPONSE_SIZE)
        return postRepository.getTopPosts(paginationParameters)
            .doOnSuccess { nextAnchor = it.nextPaginationAnchor }
            .map { paginatedResponse -> paginatedResponse.data }
            .applyIoScheduler()
    }
}