package dev.ashdavies.playground.event

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

public data class EventListState(
    val itemList: ImmutableList<dev.ashdavies.playground.event.Event?>,
    val selectedIndex: Int?,
    val isRefreshing: Boolean,
    val errorMessage: String?,
    val eventSink: (Event) -> Unit,
) : CircuitUiState {

    public sealed interface Event {
        public data class ItemClick(val id: Long) : Event
        public data object Refresh : Event
    }
}
