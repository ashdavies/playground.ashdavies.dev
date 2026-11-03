package dev.ashdavies.playground.event

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import kotlin.uuid.Uuid

public sealed interface EventListState : CircuitUiState {

    public data class Success(
        val itemList: LazyPagingItems<Item>,
        val isRefreshing: Boolean,
        val eventSink: (Event) -> Unit,
    ) : EventListState {

        public data class Item(
            public val thumbnailImageSeed: Long,
            public val uuid: Uuid,
            public val name: String,
            public val location: String,
            public val date: String,
            public val status: Status?,
            public val isBookmarked: Boolean,
        ) {

            public sealed interface Status {
                public data class Closing(val remaining: String) : Status
                public data class Opening(val remaining: String) : Status

                public data object Closed : Status
                public data object Open : Status
                public data object NotAnnounced : Status
            }
        }

        public sealed interface Event {
            public data class ToggleBookmark(val index: Int) : Event
            public data class ItemClick(val index: Int) : Event

            public data object Refresh : Event
        }
    }

    public data class Failure(
        val icon: ImageVector,
        val message: String?,
    ) : EventListState
}
