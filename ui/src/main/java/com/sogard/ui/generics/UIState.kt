package com.sogard.ui.generics

sealed class UIState(val index: Int) {

    object LoadingState : UIState(0)
    object ErrorState : UIState(1)
    object SuccessState : UIState(2)
}