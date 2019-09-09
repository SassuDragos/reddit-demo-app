package com.sogard.ui.generics

import androidx.lifecycle.MutableLiveData

/** Plus Operator for a MutableLiveData containing a list of elements. It allows adding a
 *  the elements of a second list ([values]) directly to the list stored in the LiveData object.
*/


operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: List<T>) {
    this.value?.addAll(values)
}