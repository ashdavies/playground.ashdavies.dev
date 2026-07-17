package dev.ashdavies.playground.event

import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.parcelable.Parcelable
import dev.ashdavies.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
public sealed interface EventScreen : Parcelable, Screen {

    @Parcelize
    @Serializable
    public data class List(val initialKey: Long? = null) : EventScreen

    @Parcelize
    @Serializable
    public data class Grid(val initialKey: Long? = null) : EventScreen

    @Parcelize
    @Serializable
    public data class Detail(val id: Long) : EventScreen
}
