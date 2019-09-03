package com.sogard.ui

import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.sogard.ui.comments.CommentViewModel
import com.sogard.ui.generics.BaseViewModel
import com.sogard.ui.generics.ErrorHandler
import com.sogard.ui.helpers.ResourceProvider
import com.sogard.ui.topposts.PostViewModel
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import org.koin.core.inject


class CommentListViewModel: BaseViewModel() {

    private val resourceProvider: ResourceProvider by inject()

    val errorHandler = object :ErrorHandler {
        override val errorMessage: String = resourceProvider.getString(R.string.error_failed_comments)

        override var onRetryClicked = {}
    }

    var articleId: String? = null

    var flipperPosition: MutableLiveData<Int> = MutableLiveData(0)

    val commentList: ObservableArrayList<CommentViewModel> = ObservableArrayList()
    val commentItemBinding = OnItemBindClass<CommentViewModel>()
        .map(CommentViewModel::class.java, BR.viewModel, R.layout.item_comment)

    fun loadReplies(commentId: String) {

    }

    fun loadComments(articleId: String?) {
        articleId?.let {

        }
    }
}