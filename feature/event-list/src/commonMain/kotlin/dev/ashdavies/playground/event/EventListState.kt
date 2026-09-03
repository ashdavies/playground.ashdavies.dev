package dev.ashdavies.playground.event

import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

public sealed interface EventListState : CircuitUiState {

    public data class Success(
        val itemList: ImmutableList<dev.ashdavies.playground.event.Event?>,
        val selectedIndex: Int?,
        val isRefreshing: Boolean,
        val eventSink: (Event) -> Unit,
    ) : EventListState {

        public sealed interface Event {
            public data class ItemClick(val id: Long) : Event
            public data class ItemCfpClick(val uri: String) : Event

            public data object Refresh : Event
        }
    }

    public data class Failure(
        val icon: ImageVector,
        val message: String?,
    ) : EventListState
}
