package com.sogard.presenter

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val text: MutableLiveData<Int> = MutableLiveData(0)

    fun clickBtn(view: View) {
        text.value = text.value?.plus(1)

        Toast.makeText(view.context, "Succeeded " + this.hashCode(), Toast.LENGTH_SHORT).show()
    }
}