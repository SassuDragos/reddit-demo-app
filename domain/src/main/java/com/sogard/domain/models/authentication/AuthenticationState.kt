package com.sogard.domain.models.authentication

sealed class AuthenticationState {
    object NotAuthenticated : AuthenticationState()

    class Authenticated(val token: String, val tokenExpirationTime: Long) : AuthenticationState()
}
