package com.sogard.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sogard.ui.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.view_top_posts.view.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(
                TopPostsViewModel::class.java
            )


        val binding: ActivityHomeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //TODO Replace this EndlessRecyclerView with the Paging library flow
        val recyclerView = binding.root.rv_posts
        binding.root.rv_posts.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                viewModel.loadPosts()
            }
        })
    }
}