package com.sogard.ui.toparticles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sogard.ui.helpers.NavigationHandler
import com.sogard.ui.R
import com.sogard.ui.TopArticlesViewModel
import com.sogard.ui.databinding.ActivityHomeBinding
import com.sogard.ui.generics.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.view_top_articles_list.view.*


class ArticleListingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(
                TopArticlesViewModel::class.java
            )

        val binding: ActivityHomeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //TODO Replace this EndlessRecyclerView with the Paging library flow. This one is VERY inefficient.
        val recyclerView = binding.root.rv_posts
        binding.root.rv_posts.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                viewModel.loadArticles()
            }
        })

        viewModel.navigationLiveData.observe(this, Observer { newDestination ->
            NavigationHandler.goTo(newDestination, this)
        })
    }
}