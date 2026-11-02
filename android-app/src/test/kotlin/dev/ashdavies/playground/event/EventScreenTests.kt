package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paparazzi.Paparazzi
import dev.ashdavies.playground.event.EventListState.Failure
import dev.ashdavies.playground.event.detail.EventDetailState
import dev.ashdavies.playground.event.detail.EventDetailUi
import dev.ashdavies.playground.event.grid.EventGridState
import dev.ashdavies.playground.event.grid.EventGridUi
import dev.ashdavies.playground.tooling.MaterialPreviewTheme
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.operation_not_implemented
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.junit.Rule
import org.junit.Test

private val SnapshotDate = LocalDate(2026, 1, 1)

internal class EventScreenTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun eventList() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventListUi(
                    state = EventListState.Success(
                        itemList = Json
                            .upcomingEvents()
                            .map { it.toEventListStateSuccessItem(SnapshotDate, isBookmarked = false) }
                            .toLazyPagingItems(),
                        isRefreshing = false,
                        eventSink = { },
                    ),
                )
            }
        }
    }

    @Test
    fun eventListFailure() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventListUi(
                    state = Failure(
                        icon = Icons.Outlined.CloudOff,
                        message = stringResource(Res.string.operation_not_implemented),
                    ),
                )
            }
        }
    }

    @Test
    fun eventGrid() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventGridUi(
                    state = EventGridState(
                        itemList = Json
                            .upcomingEvents()
                            .map(Event::toEventGridStateItem)
                            .toImmutableList(),
                        eventSink = { },
                    ),
                )
            }
        }
    }

    @Test
    fun eventDetail() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventDetailUi(
                    state = EventDetailState(
                        itemState = EventDetailState.ItemState.Done(
                            item = Json.upcomingEvents().first(),
                        ),
                        onBackPressed = { },
                    ),
                )
            }
        }
    }
}

private fun Event.toEventGridStateItem() = EventGridState.Item(
    title = "$name ${dateStart.year}",
    group = "${dateStart.year}",
    subtitle = location,
    attended = false,
)

@Composable
private fun <T : Any> List<T>.toLazyPagingItems(): LazyPagingItems<T> {
    val pagingData = remember(this) { MutableStateFlow(PagingData.from(this)) }
    return pagingData.collectAsLazyPagingItems()
}
