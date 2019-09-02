package com.sogard.ui.topposts

import com.sogard.domain.models.Post

class PostViewModel(
    private val post: Post,
    val onViewCommentsClicked: (String) -> Unit,
    val onPostClicked: (String) -> Unit
) {
    val title = post.title
    val totalComments = post.totalCommentNumber
}