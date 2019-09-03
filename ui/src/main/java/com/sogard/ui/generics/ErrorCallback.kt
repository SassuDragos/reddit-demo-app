package com.sogard.ui.generics

interface ErrorHandler{

    val errorMessage: String

    var onRetryClicked: () -> Unit
}