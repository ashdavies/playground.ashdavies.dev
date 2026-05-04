package dev.ashdavies.playground.event

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

internal data class EventListState(
    val itemList: ImmutableList<dev.ashdavies.playground.event.Event?>,
    val selectedIndex: Int?,
    val isRefreshing: Boolean,
    val errorMessage: String?,
    val eventSink: (Event) -> Unit,
) : CircuitUiState {

    sealed interface Event {
        data class ItemClick(val id: Long) : Event
        data object Refresh : Event
    }
}
