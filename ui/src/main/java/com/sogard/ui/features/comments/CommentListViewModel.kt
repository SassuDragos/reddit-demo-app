package com.sogard.ui.features.comments

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.SavedStateHandle
import com.sogard.domain.usecases.CommentListingUseCase
import com.sogard.ui.BR
import com.sogard.ui.R
import com.sogard.ui.generics.BaseViewModel
import com.sogard.ui.generics.ErrorUiHandler
import com.sogard.ui.generics.UIState
import com.sogard.ui.helpers.ResourceProvider
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import org.koin.core.inject


class CommentListViewModel(private val state: SavedStateHandle) : BaseViewModel() {

    private val resourceProvider: ResourceProvider by inject()
    private val commentListingUseCase: CommentListingUseCase by inject()

    val errorHandler = object : ErrorUiHandler {

        override val errorMessage: String =
            resourceProvider.getString(R.string.error_failed_comments)

        override var onRetryClicked = {
            Log.i("[RETRY: COMM. LOAD]", "Attempting to reload comments.")
            loadComments(articleId)
        }
    }

    var articleId: String? = null

    init {
        currentState.value = UIState.LoadingState
    }

    val commentList: ObservableArrayList<CommentViewModel> = ObservableArrayList()
    val commentItemBinding = OnItemBindClass<CommentViewModel>()
        .map(CommentViewModel::class.java, BR.viewModel, R.layout.item_comment)

    fun loadReplies(commentId: String) {
        //TODO:
    }

    fun loadComments(articleId: String?) {
        articleId?.let { id ->
            commentListingUseCase.loadInitialComments(id)
                .subscribe({ list ->
                    commentList.addAll(list.map { CommentViewModel(it, ::loadReplies) })
                    currentState.postValue(UIState.SuccessState)
                }, { throwable ->
                    Log.e("[ERROR COMM. LOADING]", throwable.message)
                    currentState.postValue(UIState.ErrorState)
                })
        }
    }

}