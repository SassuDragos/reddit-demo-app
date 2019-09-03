package com.sogard.domain.models

data class ReplyActivitySummary(val totalReplies: Int,
                                val id: String,
                                val replyIdList: List<String>)
data class Comment(
    val id: String,
    val author: String,
    val body: String,
//    val replyActivitySummary: ReplyActivitySummary,
    val totalReplies: Int
)