package com.sogard.ui.topposts

import com.sogard.domain.models.Post
import com.sogard.ui.PostInterface

class PostViewModel(
    post: Post,
    private val onViewCommentsClicked: (postId: String) -> Unit,
    private val onPostClicked: (postId: String, postEndpoint: String) -> Unit
) : PostInterface {
    val title = post.title
    val totalComments = post.totalCommentNumber
    val id = post.id
    private val endpoint = post.detailsUrl

    fun onViewCommentsClicked() {
        onViewCommentsClicked.invoke(id)
    }

    fun onPostClicked() {
        onPostClicked.invoke(id, endpoint)
    }
}

