package com.sogard.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableArrayList
import com.sogard.domain.usecases.TopPostsManagementUseCase
import com.sogard.ui.topposts.PostViewModel
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import org.koin.core.inject

//TODO: The splash screen and the top posts will to be in 2 separate viewModels / Views.
// I decided to temporary inherit from the SplashViewModel as it allows me ensure that I am authenticated.

class TopPostsViewModel : SplashViewModel() {

    private val topPostsManagementUseCase: TopPostsManagementUseCase by inject()

    val postList: ObservableArrayList<PostViewModel> = ObservableArrayList()
    val postItemBinding = OnItemBindClass<PostViewModel>()
        .map(PostViewModel::class.java, BR.viewModel, R.layout.item_post)

    init {
        loadPosts()
    }

    fun loadPosts() {
        val subscription = topPostsManagementUseCase
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