package com.sogard.domain.repositories

import com.sogard.domain.models.Comment
import com.sogard.domain.models.CommentsPaginationParams
import com.sogard.domain.models.PaginatedResponse
import com.sogard.domain.models.PaginationParameters
import io.reactivex.Single

interface CommentRepository {

    fun loadComments(param: CommentsPaginationParams): Single<List<Comment>>
}