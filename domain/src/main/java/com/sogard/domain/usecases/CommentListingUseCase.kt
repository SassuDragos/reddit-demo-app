package com.sogard.domain.usecases

import com.sogard.domain.models.comment.Comment
import com.sogard.domain.models.comment.CommentsPaginationParams
import com.sogard.domain.repositories.CommentRepository
import extensions.applyIoScheduler
import io.reactivex.Single

class CommentListingUseCase(private val commentRepository: CommentRepository) {

    fun getComments(articleId: String): Single<List<Comment>> = commentRepository
        .loadComments(CommentsPaginationParams(articleId, 100, 1))
        .applyIoScheduler()
}