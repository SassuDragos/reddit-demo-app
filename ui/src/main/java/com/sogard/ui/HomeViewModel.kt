package com.sogard.ui

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sogard.domain.usecases.ApplicationInitializationUseCase
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

import me.tatarka.bindingcollectionadapter2.ItemBinding
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import com.sogard.domain.models.Post
import com.sogard.ui.topposts.PostViewModel


class HomeViewModel() : ViewModel(), KoinComponent {

    private val disposable: CompositeDisposable = CompositeDisposable()
    val appInitUseCase: ApplicationInitializationUseCase by inject<ApplicationInitializationUseCase>()

    val postList = MutableLiveData<List<PostViewModel>>()
    val postItemBinding = ItemBinding.of<PostViewModel>(BR.viewModel, R.layout.item_post)

    init {
        val subscription = appInitUseCase
            .initializeApplication()
            .subscribe({
                text.value = 9999
            }, {
                text.value = -1
                Log.e("[APP INIT FAILED] ", it.message)
            })
        disposable.add(subscription)

        val dummyPostList = listOf(
            PostViewModel(Post("1", "What is your default thought? When you are bored, what is the first thing you tend to fantasize about?", "", 20231, ""), {}, {}),
            PostViewModel(Post("2", "\"I said we're equal.\" The guy has zen level control over his actions.", "", 20231, ""), {}, {}),
            PostViewModel(Post("2", "AITA For Telling My Wife That If She Quits Her Job I Expect Her To Cover All The Housework?", "", 20231, ""), {}, {}))
        postList.postValue(dummyPostList.plus(dummyPostList).plus(dummyPostList).plus(dummyPostList))
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }

    val text: MutableLiveData<Int> = MutableLiveData(0)

    fun clickBtn(view: View) {
        Toast.makeText(view.context, "Succeeded " + this.hashCode(), Toast.LENGTH_SHORT).show()
        val subscription = appInitUseCase
            .initializeApplication()
            .subscribe({
                text.value = 9999
            }, {
                text.value = -1
                Log.e("[APP INIT FAILED] ", it.message)
            })
        disposable.add(subscription)
    }
}