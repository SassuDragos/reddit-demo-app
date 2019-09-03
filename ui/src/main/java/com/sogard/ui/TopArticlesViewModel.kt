package com.sogard.ui

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.sogard.domain.usecases.TopArticleListingUseCase
import com.sogard.ui.toparticles.PostViewModel
import com.sogard.ui.toparticles.SplashViewModel
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import org.koin.core.inject

//TODO: The splash screen and the top posts will to be in 2 separate viewModels / Views.
// I decided to temporary inherit from the SplashViewModel as it allows me ensure that I am authenticated.

class TopArticlesViewModel : SplashViewModel() {

    private val topArticleListingUseCase: TopArticleListingUseCase by inject()

    val postList: ObservableArrayList<PostViewModel> = ObservableArrayList()
    val postItemBinding = OnItemBindClass<PostViewModel>()
        .map(PostViewModel::class.java, BR.viewModel, R.layout.item_post)

    init {
        loadPosts()
    }

    fun loadPosts() {
        val subscription = topArticleListingUseCase
            .getPosts(postList.size)
            .subscribe({ posts ->
                val postVMList =
                    posts.map { PostViewModel(it, navigationLiveData) }
                postList += postVMList
            }, {
                Log.e("[POST LOADING FAILED]", it.message)
            })
        subscriptions.add(subscription)
    }
}