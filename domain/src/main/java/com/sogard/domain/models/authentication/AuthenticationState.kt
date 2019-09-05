package com.sogard.domain.models.authentication

sealed class AuthenticationState {
    object NotAuthenticated : AuthenticationState()

    object Authenticated : AuthenticationState()
}
