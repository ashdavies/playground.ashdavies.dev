package io.ashdavies.playground.profile

internal sealed class ProfileViewState {

    data class LogIn(val uriString: String) : ProfileViewState()

    data class LoggedIn(val name: String, val email: String, val picture: String) : ProfileViewState()

    object LoggedOut : ProfileViewState()
}
