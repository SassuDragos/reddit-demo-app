package com.sogard.ui.comments

import com.sogard.domain.models.comment.Comment


class CommentViewModel(
    comment: Comment,
    private val loadReplies: (commentId: String) -> Unit
) {
    val id = comment.id
    val author: String = comment.id
    val body: String = comment.body
    val totalReplies: Int = comment.totalReplies

    fun onShowMoreRepliesClicked() {
        loadReplies.invoke(id)
    }
}