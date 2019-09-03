package com.sogard.domain.repositories

import com.sogard.domain.models.PaginatedResponse
import com.sogard.domain.models.PaginationParameters
import com.sogard.domain.models.Post
import io.reactivex.Single

interface PostRepository {
    fun getTopPosts(pagParam: PaginationParameters): Single<PaginatedResponse<List<Post>>>
}