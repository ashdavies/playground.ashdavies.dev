package dev.ashdavies.playground.event.history

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlin.uuid.Uuid

public data class EventHistoryState(
    public val itemList: ImmutableList<Item>,
    public val eventSink: (Event) -> Unit,
) : CircuitUiState {

    public data class Item(
        public val thumbnailImageSeed: Long,
        public val uuid: Uuid,
        public val name: String,
        public val year: String,
    )

    public sealed interface Event : CircuitUiEvent {
        public data class ItemClick(val index: Int) : Event
    }
}
