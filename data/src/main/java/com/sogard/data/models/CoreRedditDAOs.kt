package com.sogard.data.models

import com.squareup.moshi.Json

sealed class CoreRedditDAO

/**
 * [children]: The list of elements ("a slice of the list") requested from the API.
 * [after]: The name of the element which follows the items provided in the [children] list. Reddit returns
 * only a slice of the existing list of elements (e.g. list of posts, list of comments, list of reddits, etc.).
 * the [after] parameter can be used as an anchor point for the next [children] slice.
 * [before]: The name of the element which precedes the items provided in the [children] list.
 * */
data class ListingData<T>(
    val children: List<T>,
    @field:Json(name = "before") val previousAnchor: String?,
    @field:Json(name = "after") val nextAnchor: String?
) : CoreRedditDAO()

data class ArticleDAO(
    val id: String,
    val title: String,
    val name: String,
    @field:Json(name = "author_fullname") val authorName: String,
    @field:Json(name = "num_comments") val totalComments: Int,
    @field:Json(name = "permalink") val endpoint: String
) : CoreRedditDAO()

data class CommentDAO(
    val id: String,
    @field:Json(name = "author_fullname") val authorName: String,
    @field:Json(name = "num_comments") val totalComments: Int,
//    @field:Json(name = "replies") val replies: DataWrapper<ListingData<DataWrapper<CommentReplySummaryDAO>>>,
    val body: String
) : CoreRedditDAO()

data class MoreDataDAO(
    val id: String,
    @field:Json(name = "count") val totalUndiscoveredElements: Int,
    @field:Json(name = "parent_id") val parentId: String,
    val depth: Int,
    val children: List<String>
) : CoreRedditDAO()