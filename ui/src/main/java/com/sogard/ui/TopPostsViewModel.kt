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

interface PostInterface

class TopPostsViewModel : SplashViewModel() {
    
    private val topPostsManagementUseCase: TopPostsManagementUseCase by inject()

    private val onViewCommentsClicked: (String) -> Unit = { postId ->  }
    private val onPostClicked: (String, String) -> Unit = { postId, postEndpoint -> }

    val postList: ObservableArrayList<PostInterface> = ObservableArrayList()
    val postItemBinding = OnItemBindClass<PostInterface>()
        .map(PostViewModel::class.java, BR.viewModel, R.layout.item_post)

    init {
        loadPosts()
    }

    fun loadPosts() {
        val subscription = topPostsManagementUseCase
            .getPosts(postList.size)
            .subscribe({ posts ->
                val postVMList =
                    posts.map { PostViewModel(it, onViewCommentsClicked, onPostClicked) }
                postList += postVMList
            }, {
                Log.e("[POST LOADING FAILED]", it.message)
            })
        subscriptions.add(subscription)
    }
}