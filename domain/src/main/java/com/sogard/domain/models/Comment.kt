package com.sogard.domain.models

data class Comment(
    val id: String,
    val author: String,
    val body: String,
    val totalReplies: Int
)