package com.sogard.ui.features.comments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.sogard.ui.R
import com.sogard.ui.databinding.ActivityCommentsBinding
import com.sogard.ui.helpers.NavigationKeys.ARTICLE_ID
import org.koin.core.KoinComponent


class CommentsActivity : AppCompatActivity(), KoinComponent {

    private var articleId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        articleId = intent?.extras?.getString(ARTICLE_ID) ?: savedInstanceState?.getString("ARTICLE_ID")

        val viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(
                CommentListViewModel::class.java
            )

        val binding: ActivityCommentsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_comments)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadComments(articleId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("ARTICLE_ID", articleId)
    }
}