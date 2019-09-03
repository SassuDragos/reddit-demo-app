package com.sogard.ui.generics

import androidx.lifecycle.MutableLiveData

operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: List<T>) {
    this.value?.addAll(values)
}