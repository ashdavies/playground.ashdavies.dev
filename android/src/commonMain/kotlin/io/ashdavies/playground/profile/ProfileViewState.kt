package io.ashdavies.playground.profile

import io.ashdavies.playground.Profile

internal sealed class ProfileViewState {

    data class LoggedIn(
        val name: String,
        val picture: String?,
        val position: String?,
        val location: String?,
    ) : ProfileViewState() {

        constructor(profile: Profile) : this(
            name = profile.name,
            picture = profile.picture,
            position = profile.position,
            location = profile.location
        )
    }

    object LoggedOut : ProfileViewState()
}
