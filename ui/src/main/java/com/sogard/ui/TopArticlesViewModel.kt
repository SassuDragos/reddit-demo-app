package com.sogard.ui

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.sogard.domain.usecases.TopArticleListingUseCase
import com.sogard.ui.toparticles.ArticleViewModel
import com.sogard.ui.toparticles.SplashViewModel
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import org.koin.core.inject

//TODO: The splash screen and the Top Article list will to be in 2 separate viewModels / Views.
// I decided to temporary inherit from the SplashViewModel as it allows (more or less) to be authenticated.

//TODO: there is a bug with AS and I can't move this to the top articles package.
class TopArticlesViewModel : SplashViewModel() {

    private val topArticleListingUseCase: TopArticleListingUseCase by inject()

    val articleList: ObservableArrayList<ArticleViewModel> = ObservableArrayList()
    val articleItemBinding = OnItemBindClass<ArticleViewModel>()
        .map(ArticleViewModel::class.java, BR.viewModel, R.layout.item_article)

    init {
        loadArticles()
    }

    fun loadArticles() {
        val subscription = topArticleListingUseCase
            .getArticles(articleList.size)
            .subscribe({ articles ->
                val articleVMList =
                    articles.map { ArticleViewModel(it, navigationLiveData) }
                articleList += articleVMList
            }, {
                Log.e("[ERROR:ARTICLE LOADING]", it.message)
            })
        subscriptions.add(subscription)
    }
}