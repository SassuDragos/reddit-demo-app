package com.sogard.ui.comments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.sogard.ui.CommentListViewModel
import com.sogard.ui.helpers.NavigationKeys.ARTICLE_ID
import com.sogard.ui.R
import com.sogard.ui.databinding.ActivityCommentsBinding
import org.koin.core.KoinComponent


class CommentsActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent?.extras?.getString(ARTICLE_ID)

        val viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(
                CommentListViewModel::class.java
            ).apply {
                //TODO: Write a ViewModel Factory class that allows for providing custom constructor parameters.
                // This will allow the viewModel to instantiate the articleId during construction.
                // Consequently, articleId will no longer be nullable.
                articleId = id
                loadComments(articleId)
            }

        val binding: ActivityCommentsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_comments)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

}