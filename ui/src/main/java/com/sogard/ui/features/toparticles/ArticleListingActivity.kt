package com.sogard.ui.features.toparticles

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sogard.ui.R
import com.sogard.ui.databinding.ActivityHomeBinding
import com.sogard.ui.generics.EndlessRecyclerViewScrollListener
import com.sogard.ui.generics.navigation.NavigationAwareActivity
import com.sogard.ui.generics.navigation.NavigationResolver
import kotlinx.android.synthetic.main.view_top_articles_list.view.*

class ArticleListingActivity : NavigationAwareActivity<TopArticlesViewModel>() {

    override fun getNavigationResolver() = NavigationResolver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityHomeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //TODO Replace this EndlessRecyclerView with the Paging library flow. This one is VERY inefficient.
        val recyclerView = binding.root.rv_articles
        binding.root.rv_articles.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                viewModel?.loadArticles()
            }
        })
    }

    override fun createViewModel(): TopArticlesViewModel =
        ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(
            TopArticlesViewModel::class.java
        )
}