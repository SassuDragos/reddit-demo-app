package com.sogard.domain.repositories

import com.sogard.domain.models.PaginatedResponse
import com.sogard.domain.models.Post
import io.reactivex.Single

interface PostRepository {
    fun getTopPosts(nextAnchor: String?, totalLoadedItems: Int, maxListSize: Int): Single<PaginatedResponse<List<Post>>>
}