package com.sogard.presenter

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sogard.domain.usecases.ApplicationInitializationUseCase
import io.reactivex.disposables.CompositeDisposable


class HomeViewModel(appInitUseCase: ApplicationInitializationUseCase) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

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
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }

    val text: MutableLiveData<Int> = MutableLiveData(0)

    fun clickBtn(view: View) {
        Toast.makeText(view.context, "Succeeded " + this.hashCode(), Toast.LENGTH_SHORT).show()
    }
}