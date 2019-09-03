package com.sogard.data.models

import com.squareup.moshi.Json

data class CommentDAO(
    val title: String,
    @field:Json(name = "name") val id: String,
    @field:Json(name = "author_fullname") val authorName: String,
    @field:Json(name = "num_comments") val totalComments: Int,
    @field:Json(name = "permalink") val endpoint: String
)