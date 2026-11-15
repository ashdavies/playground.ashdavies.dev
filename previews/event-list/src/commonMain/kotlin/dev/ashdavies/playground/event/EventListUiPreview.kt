package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.ashdavies.playground.tooling.MaterialThemeWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiSuccessPreview() {
    EventListUi(
        state = EventListState.Success(
            itemList = EventFixtures
                .upcoming { it.toEventListStateSuccessItem() }
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

private fun DomainWrapper<DomainEvent>.toEventListStateSuccessItem() = EventListState.Success.Item.Standard(
    thumbnailImageSeed = Random.nextLong(),
    header = formatDateHeader(value.dateStart),
    uuid = value.uuid,
    name = value.name,
    location = value.location,
    date = formatDateRange(value.dateStart, value.dateEnd),
    status = when {
        attrs["cfpStart"] != null -> EventListState.Success.Item.Standard.Status.Open
        else -> EventListState.Success.Item.Standard.Status.NotAnnounced
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
