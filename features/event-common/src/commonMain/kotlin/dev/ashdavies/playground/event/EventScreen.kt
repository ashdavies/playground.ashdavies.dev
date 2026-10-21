package dev.ashdavies.playground.event

import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.serialization.CircuitSerializable
import dev.zacsweers.metro.AppScope
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
@CircuitSerializable(AppScope::class)
public sealed interface EventScreen : Screen {

    @Serializable
    public data class List(val initialKey: Int? = null) : EventScreen

    @Serializable
    public data class Grid(val initialKey: Int? = null) : EventScreen

    @Serializable
    public data class Detail(val uuid: Uuid) : EventScreen
}
