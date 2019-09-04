package com.sogard.ui.generics

interface ErrorUiHandler{

    val errorMessage: String

    var onRetryClicked: () -> Unit
}