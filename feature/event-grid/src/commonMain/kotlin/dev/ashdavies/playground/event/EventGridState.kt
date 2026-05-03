package dev.ashdavies.playground.event

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

public data class EventGridState(
    val itemList: ImmutableList<Item>,
    val eventSink: (Event) -> Unit,
) : CircuitUiState {

    public sealed interface Event {
        public data class MarkAttendance(
            val id: String,
            val value: Boolean,
        ) : Event
    }

    public data class Item(
        val uuid: String,
        val title: String,
        val group: String,
        val subtitle: String,
        val attended: Boolean,
    )
}
