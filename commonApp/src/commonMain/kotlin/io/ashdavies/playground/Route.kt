package io.ashdavies.playground

sealed class Route(val name: String) {

    object Events : Route("events")
    object Profile : Route("profile")

    companion object {
        val routes get() = listOf(Events, Profile)
    }
}
