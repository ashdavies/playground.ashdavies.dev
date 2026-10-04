package dev.ashdavies.playground.event

import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.runtime.CircuitUiState
import dev.ashdavies.playground.event.EventListState.Success.Sorting
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public sealed interface EventListState : CircuitUiState {

    public data class Success(
        val itemList: ImmutableList<Item?>,
        val searchResults: ImmutableList<String>,
        val sorting: Sorting,
        val isRefreshing: Boolean,
        val eventSink: (Event) -> Unit,
    ) : EventListState {

        public data class Item(
            public val status: Status?,
            public val trackCount: Int?,
            public val series: String?,
            public val name: String,
            public val location: String,
            public val dateSubtitle: String,
            public val dateTitle: String,
            public val tagList: ImmutableList<String>,
            public val attendeeCount: Int?,
            public val isBookmarked: Boolean,
        ) {

            public enum class Status {
                CFP_CLOSED,
                CFP_OPEN,
                REGISTRATION_OPEN,
            }
        }

        public enum class Sorting {
            ASCENDING,
            DESCENDING,
        }

        public sealed interface Event {
            public data class ItemClick(val index: Int) : Event
            public data class Search(val query: String) : Event

            public data object ToggleSorting : Event
            public data object Refresh : Event
        }
    }

    public data class Failure(
        val icon: ImageVector,
        val message: String?,
    ) : EventListState

    public companion object {
        public val Initial: EventListState = Success(
            itemList = persistentListOf(),
            searchResults = persistentListOf(),
            sorting = Sorting.DESCENDING,
            isRefreshing = true,
            eventSink = { },
        )
    }
}
