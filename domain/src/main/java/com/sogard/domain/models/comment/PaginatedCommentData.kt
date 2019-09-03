package com.sogard.domain.models.comment

//TODO: Review the API once again. It may be that we can abstract the pagination data

/**
 * [data]: The list of requested comments.
 *
 * [moreCommentIdList]: A list with the comment ids which are available for loading at the same tree
 *                      depth as the [data].
 */
data class PaginatedCommentData(
    val data: List<Comment>,
    val moreCommentIdList: List<String>)