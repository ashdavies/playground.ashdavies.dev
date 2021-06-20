package io.ashdavies.playground

sealed class Route(val name: String) {

    object Conferences : Route("conferences")
    object Profile : Route("profile")

    companion object {
        val routes get() = listOf(Conferences, Profile)
    }
}
