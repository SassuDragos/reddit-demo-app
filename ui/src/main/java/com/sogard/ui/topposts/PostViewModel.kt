package com.sogard.ui.topposts

import androidx.lifecycle.MutableLiveData
import com.sogard.domain.models.Post
import com.sogard.ui.NavigationDestination
import com.sogard.ui.NavigationDestination.CommentsDestination
import com.sogard.ui.NavigationDestination.UrlDestination
import com.sogard.ui.PostInterface

class PostViewModel(
    post: Post,
    private val navigationListener: MutableLiveData<NavigationDestination>
) : PostInterface {
    val title = post.title
    val totalComments = post.totalCommentNumber
    val id = post.id
    private val detailsUrl = post.detailsUrl

    fun onViewCommentsClicked() = navigationListener.postValue(CommentsDestination(id))
    fun onPostClicked() = navigationListener.postValue(UrlDestination(detailsUrl))
}

