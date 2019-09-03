package com.sogard.ui.toparticles

import androidx.lifecycle.MutableLiveData
import com.sogard.domain.models.article.Article
import com.sogard.ui.helpers.NavigationDestination
import com.sogard.ui.helpers.NavigationDestination.CommentsDestination
import com.sogard.ui.helpers.NavigationDestination.UrlDestination

class ArticleViewModel(
    article: Article,
    private val navigationListener: MutableLiveData<NavigationDestination>
) {
    val title = article.title
    val totalComments = article.totalCommentNumber
    val id = article.id
    private val detailsUrl = article.detailsUrl

    fun onViewCommentsClicked() = navigationListener.postValue(CommentsDestination(id))
    fun onArticleClicked() = navigationListener.postValue(UrlDestination(detailsUrl))
}

