package com.sogard.domain.repositories

import com.sogard.domain.models.comment.Comment
import com.sogard.domain.models.comment.CommentsPaginationParams
import com.sogard.domain.models.comment.PaginatedCommentData
import io.reactivex.Single

interface CommentRepository {

    fun loadComments(param: CommentsPaginationParams): Single<PaginatedCommentData>
}