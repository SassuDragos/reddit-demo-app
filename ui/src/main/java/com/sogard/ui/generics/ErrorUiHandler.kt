package com.sogard.ui.generics

/**
 * Interface use for mapping the generic Error UI which contains a text error message and a retry button.
 * @see(R.layout.view_error)
 * */
interface ErrorUiHandler{

    val errorMessage: String

    var onRetryClicked: () -> Unit
}