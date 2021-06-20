package io.ashdavies.playground

sealed class Route(val name: String) {

    object Events : Route("Events")
    object Profile : Route("Profile")

    companion object {
        val routes get() = listOf(Events, Profile)
    }
}
