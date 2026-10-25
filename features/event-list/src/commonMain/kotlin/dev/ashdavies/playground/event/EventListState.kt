package dev.ashdavies.playground.event

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

public sealed interface EventListState : CircuitUiState {

    public data class Success(
        val itemList: ImmutableList<Item?>,
        val searchResults: ImmutableList<String>,
        val isRefreshing: Boolean,
        val eventSink: (Event) -> Unit,
    ) : EventListState {

        public data class Item(
            public val thumbnailImageSeed: Long,
            public val name: String,
            public val location: String,
            public val date: String,
            public val status: StatusBadge?,
            public val isBookmarked: Boolean,
        ) {

            public data class StatusBadge(
                public val containerColor: Color,
                public val contentColor: Color,
                public val icon: ImageVector?,
                public val text: String,
            )
        }

        public sealed interface Event {
            public data class ToggleBookmark(val index: Int) : Event
            public data class ItemClick(val index: Int) : Event
            public data class Search(val query: String) : Event

            public data object Refresh : Event
        }
    }

    public data class Failure(
        val icon: ImageVector,
        val message: String?,
    ) : EventListState

    public companion object {
        public val Initial: EventListState = Success(
            itemList = List(8) { null }.toPersistentList(),
            searchResults = persistentListOf(),
            isRefreshing = true,
            eventSink = { },
        )
    }
}
