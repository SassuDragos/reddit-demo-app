package com.sogard.data.repositories

import com.sogard.data.apis.CommentsApi
import com.sogard.data.models.CommentDAO
import com.sogard.data.models.getContent
import com.sogard.domain.models.comment.Comment
import com.sogard.domain.models.comment.CommentsPaginationParams
import com.sogard.domain.repositories.CommentRepository
import io.reactivex.Single

class CommentRepositoryImpl(private val commentsApi: CommentsApi) : CommentRepository {
    override fun loadComments(param: CommentsPaginationParams): Single<List<Comment>> =
        commentsApi.getCommentsForPost(param.articleId, param.maxListSize, param.maxDepth)
            .map { it[1] } // it[0] is the post and it[1] is the comment data
            .map { wrapper -> wrapper.getContent().map { it.toComment() } }

    private fun CommentDAO.toComment(): Comment =
        Comment(id, authorName, body, totalComments)
}