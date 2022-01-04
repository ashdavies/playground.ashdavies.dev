package io.ashdavies.playground

public sealed class Route(public val name: String) {

    public object Events : Route("events")
    public object Profile : Route("profile")

    public companion object {
        public val routes: List<Route> get() = listOf(Events, Profile)
    }
}
