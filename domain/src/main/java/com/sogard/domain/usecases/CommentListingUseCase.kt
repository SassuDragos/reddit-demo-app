package com.sogard.domain.usecases

import com.sogard.domain.models.comment.Comment
import com.sogard.domain.models.comment.CommentsPaginationParams
import com.sogard.domain.repositories.CommentRepository
import extensions.applyIoScheduler
import io.reactivex.Single

class CommentListingUseCase(private val commentRepository: CommentRepository) {

    val moreCommentsIdList: MutableList<String> = mutableListOf()

    //TODO: It would be a better idea to allow the UseCase/ViewModel to decide how many comments to load.
    fun loadInitialComments(articleId: String): Single<List<Comment>> = commentRepository
        .loadComments(CommentsPaginationParams(articleId, 50, 1))
        .map { moreCommentsIdList.addAll(it.moreCommentIdList); it.data }
        .applyIoScheduler()

}