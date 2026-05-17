package dev.ashdavies.playground.event.detail

import com.slack.circuit.runtime.CircuitUiState
import dev.ashdavies.playground.event.Event

public data class EventDetailState(
    public val itemState: ItemState,
    public val onBackPressed: () -> Unit,
) : CircuitUiState {

    public sealed interface ItemState {
        public data object Loading : ItemState
        public data class Done(val item: Event) : ItemState
    }
}