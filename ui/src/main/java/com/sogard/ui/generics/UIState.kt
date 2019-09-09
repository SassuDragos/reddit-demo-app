package com.sogard.ui.generics

/** Sealed class modeling the three main screen states.
 * */

sealed class UIState(val index: Int) {

    object Loading : UIState(0)
    object Error : UIState(1)
    object Success : UIState(2)
}