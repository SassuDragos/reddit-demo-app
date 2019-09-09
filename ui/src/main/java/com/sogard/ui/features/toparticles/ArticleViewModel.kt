package com.sogard.ui.features.toparticles

import androidx.lifecycle.MutableLiveData
import com.sogard.domain.models.article.Article
import com.sogard.ui.generics.navigation.NavigationAction
import com.sogard.ui.generics.navigation.NavigationAction.CommentsAction
import com.sogard.ui.generics.navigation.NavigationAction.UrlAction

class ArticleViewModel(
    article: Article,
    private val navigationListener: MutableLiveData<NavigationAction>
) {
    val title = article.title
    val totalComments = article.totalCommentNumber
    val id = article.id
    private val detailsUrl = article.detailsUrl

    fun onViewCommentsClicked() = navigationListener.postValue(CommentsAction(id, true))
    fun onArticleClicked() = navigationListener.postValue(UrlAction(detailsUrl))
}

