package com.sogard.ui.features.comments

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.sogard.ui.R
import com.sogard.ui.databinding.ActivityCommentsBinding
import com.sogard.ui.generics.navigation.NavigationAwareActivity
import com.sogard.ui.generics.navigation.NavigationResolver
import com.sogard.ui.generics.navigation.NavigationKeys.ARTICLE_ID
import org.koin.core.KoinComponent

class CommentsActivity : NavigationAwareActivity<CommentListViewModel>(), KoinComponent {
    private var articleId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        articleId =
            intent?.extras?.getString(ARTICLE_ID) ?: savedInstanceState?.getString("ARTICLE_ID")

        val binding: ActivityCommentsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_comments)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel?.loadComments(articleId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("ARTICLE_ID", articleId)
    }

    override fun createViewModel(): CommentListViewModel =
        ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(
            CommentListViewModel::class.java
        )

    override fun getNavigationResolver() = NavigationResolver(this)
}