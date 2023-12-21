package io.ashdavies.gallery

internal sealed interface AuthState {
    data class Authenticated(val profilePictureUrl: String) : AuthState

    data object Unauthenticated : AuthState
    data object Unsupported : AuthState
}
