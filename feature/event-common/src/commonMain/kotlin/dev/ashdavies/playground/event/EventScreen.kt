package dev.ashdavies.playground.event

import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.parcelable.Parcelable
import dev.ashdavies.parcelable.Parcelize

public interface EventScreen : Parcelable, Screen {

    @Parcelize
    public data class List(val initialKey: Long) : EventScreen

    @Parcelize
    public data class Detail(val id: Long) : EventScreen
}
