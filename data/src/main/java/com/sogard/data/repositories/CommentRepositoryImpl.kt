package com.sogard.data.repositories

import com.sogard.data.apis.CommentsApi
import com.sogard.data.models.*
import com.sogard.domain.models.comment.Comment
import com.sogard.domain.models.comment.CommentsPaginationParams
import com.sogard.domain.models.comment.PaginatedCommentData
import com.sogard.domain.repositories.CommentRepository
import io.reactivex.Single

/**
* Implementation of the CommentRepository.
* */
class CommentRepositoryImpl(private val commentsApi: CommentsApi) : CommentRepository {

    /**
     * Method that loads a list of comments using some configuration data.
     * NOTE: the API result contains a list with 2 (-? no documentation found) objects: the source article
     * and a wrapped list of comments.
     * */
    override fun loadComments(param: CommentsPaginationParams): Single<PaginatedCommentData> =
        commentsApi.getCommentsForArticle(param.articleId, param.maxListSize, param.maxDepth)
            .map { it[1] } // it[0] is the article associated to the comments and it[1] is the comment data
            .mapToPaginatedCommentData()

    /**
     * Every list of comments returned by the server can contain a [MoreDataDAO] object.
     */
    private fun Single<ListingWrapper<CoreRedditDAO>>.mapToPaginatedCommentData(): Single<PaginatedCommentData> =
        this.map { wrapper ->
            val coreDAOList = wrapper.getContent()
            val commentList = coreDAOList.filterIsInstance<CommentDAO>().map { it.toComment() }
            val moreDataDAO = coreDAOList.lastOrNull { it is MoreDataDAO } as? MoreDataDAO
            val moreAvailableComments = moreDataDAO?.children ?: emptyList()
            PaginatedCommentData(commentList, moreAvailableComments)

        }

    private fun CommentDAO.toComment(): Comment =
        Comment(id, authorName, body, 100)
}