package dev.ashdavies.playground.event

import com.slack.circuit.runtime.screen.Screen
import kotlinx.serialization.Serializable

@Serializable
public sealed interface EventScreen : Screen {

    @Serializable
    public data class List(val initialKey: Long? = null) : EventScreen

    @Serializable
    public data class Grid(val initialKey: Long? = null) : EventScreen

    @Serializable
    public data class Detail(val id: Long) : EventScreen
}
