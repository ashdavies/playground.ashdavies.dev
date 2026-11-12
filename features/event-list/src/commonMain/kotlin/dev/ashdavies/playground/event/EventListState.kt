package dev.ashdavies.playground.event

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import kotlin.uuid.Uuid

public sealed interface EventListState : CircuitUiState {

    public data class Success(
        public val itemList: LazyPagingItems<Item>,
        public val isRefreshing: Boolean,
        public val eventSink: (Event) -> Unit,
    ) : EventListState {

        public sealed interface Item {
            public data class Standard(
                public val thumbnailImageSeed: Long,
                public val header: String,
                public val uuid: Uuid,
                public val name: String,
                public val location: String,
                public val date: String,
                public val status: Status?,
                public val isBookmarked: Boolean,
            ) : Item {

                public sealed interface Status {
                    public data class Closing(val remaining: String) : Status
                    public data class Opening(val remaining: String) : Status

                    public data object Closed : Status
                    public data object Open : Status
                    public data object NotAnnounced : Status
                }
            }

            public data class Separator(val header: String) : Item
        }

        public sealed interface Event {
            public data class ToggleBookmark(val index: Int) : Event
            public data class ItemClick(val index: Int) : Event

            public data object Refresh : Event
        }
    }

    public data class Failure(
        public val icon: ImageVector,
        public val message: String?,
    ) : EventListState
}
