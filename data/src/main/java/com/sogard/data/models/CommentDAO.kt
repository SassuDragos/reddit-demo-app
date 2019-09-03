package com.sogard.data.models

import com.squareup.moshi.Json

data class CommentReplySummaryDAO(
    val count: Int,
    val id: String,
    val children: List<String>
)

data class CommentDAO(
    @field:Json(name = "name") val id: String,
    @field:Json(name = "author_fullname") val authorName: String,
    @field:Json(name = "num_comments") val totalComments: Int,
//    @field:Json(name = "replies") val replies: DataWrapper<ListingDataWrapper<DataWrapper<CommentReplySummaryDAO>>>,
    val body: String
)