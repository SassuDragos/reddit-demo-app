package com.sogard.data.repositories

import com.sogard.data.apis.CommentsApi
import com.sogard.data.models.*
import com.sogard.domain.models.comment.Comment
import com.sogard.domain.models.comment.CommentsPaginationParams
import com.sogard.domain.models.comment.PaginatedCommentData
import com.sogard.domain.repositories.CommentRepository
import io.reactivex.Single

class CommentRepositoryImpl(private val commentsApi: CommentsApi) : CommentRepository {
    override fun loadComments(param: CommentsPaginationParams): Single<PaginatedCommentData> =
        commentsApi.getCommentsForPost(param.articleId, param.maxListSize, param.maxDepth)
            .map { it[1] } // it[0] is the article associated to the comments and it[1] is the comment data
            .mapToPaginatedCommentData()

    private fun Single<ListingWrapper<DataWrapper<CoreRedditDAO>>>.mapToPaginatedCommentData(): Single<PaginatedCommentData> =
        this.map { wrapper ->
            val coreDAOList = wrapper.getContent()
            val commentList = coreDAOList.filterIsInstance<CommentDAO>().map { it.toComment() }
            val moreDataDAO = coreDAOList.lastOrNull { it is MoreDataDAO } as? MoreDataDAO
            val moreAvailableComments = moreDataDAO?.children ?: emptyList()
            PaginatedCommentData(commentList, moreAvailableComments)

        }

    private fun CommentDAO.toComment(): Comment =
        Comment(id, authorName, body, totalComments)
}