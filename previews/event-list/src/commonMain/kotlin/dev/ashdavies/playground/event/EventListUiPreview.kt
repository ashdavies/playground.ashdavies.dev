package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.ashdavies.asg.AsgConference
import dev.ashdavies.playground.tooling.MaterialThemeWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlin.uuid.Uuid

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiSuccessPreview() {
    EventListUi(
        state = EventListState.Success(
            itemList = Json
                .asgUpcomingConferences { it.toEventListStateSuccessItem() }
                .toLazyPagingItems(),
            isRefreshing = false,
            eventSink = { },
        ),
    )
}

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiInitialPreview() {
    EventListUi(
        state = EventListState.Success(
            itemList = emptyList<EventListState.Success.Item>().toLazyPagingItems(placeholdersAfter = 6),
            isRefreshing = true,
            eventSink = { },
        ),
    )
}

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiFailurePreview() {
    EventListUi(
        state = EventListState.Failure(
            icon = Icons.Outlined.CloudOff,
            message = "Something went wrong",
        ),
    )
}

private fun AsgConference.toEventListStateSuccessItem() = EventListState.Success.Item(
    thumbnailImageSeed = Random.nextLong(),
    uuid = Uuid.random(),
    name = name,
    location = location,
    date = formatDateRange(dateStart, dateEnd),
    status = when {
        cfp != null -> EventListState.Success.Item.Status.Open
        else -> EventListState.Success.Item.Status.NotAnnounced
    },
    isBookmarked = Random.nextBoolean(),
)

@Composable
private fun <T : Any> List<T>.toLazyPagingItems(
    placeholdersAfter: Int = 0,
): LazyPagingItems<T> = PagingData
    .from(this, placeholdersAfter = placeholdersAfter)
    .let(::MutableStateFlow)
    .collectAsLazyPagingItems()
