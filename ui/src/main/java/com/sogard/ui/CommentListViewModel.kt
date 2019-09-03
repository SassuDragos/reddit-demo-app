package com.sogard.ui

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.sogard.domain.usecases.CommentListingUseCase
import com.sogard.ui.comments.CommentViewModel
import com.sogard.ui.generics.BaseViewModel
import com.sogard.ui.generics.ErrorHandler
import com.sogard.ui.helpers.ResourceProvider
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import org.koin.core.inject
import com.sogard.ui.R

sealed class ResultState(val flipperIndex: Int) {

    object LoadingState : ResultState(0)
    object ErrorState : ResultState(1)
    object SuccessState : ResultState(2)
}

//TODO: there is a bug with AS and I can't move this file to the comments package.
class CommentListViewModel : BaseViewModel() {

    private val resourceProvider: ResourceProvider by inject()
    private val commentListingUseCase: CommentListingUseCase by inject()

    val errorHandler = object : ErrorHandler {
        override val errorMessage: String =
            resourceProvider.getString(R.string.error_failed_comments)

        override var onRetryClicked = {
            Log.i("[RETRY: COMM. LOAD]", "Attempting to reload comments.")
            loadComments(articleId) }
    }

    var articleId: String? = null
    var uiState: MutableLiveData<ResultState> = MutableLiveData(ResultState.LoadingState)

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
                    uiState.postValue(ResultState.SuccessState)
                }, { throwable ->
                    Log.e("[ERROR COMM. LOADING]", throwable.message)
                    uiState.postValue(ResultState.ErrorState)
                })
        }
    }
}