package com.sogard.ui.topposts

import com.sogard.domain.models.Post

class PostViewModel(
    post: Post,
    val onViewCommentsClicked: () -> Unit,
    val onPostClicked: () -> Unit
) {
    val title = post.title
    val totalComments = post.totalCommentNumber
}