package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.Density
import dev.ashdavies.playground.event.EventListState.Failure
import dev.ashdavies.playground.event.detail.EventDetailState
import dev.ashdavies.playground.event.detail.EventDetailUi
import dev.ashdavies.playground.event.history.EventHistoryState
import dev.ashdavies.playground.event.history.EventHistoryUi
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
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

private val SnapshotDate = LocalDate(2026, 1, 1)

@RunWith(Parameterized::class)
internal class EventScreenTests(deviceConfig: DeviceConfig) {

    private val factory = eventListItemFactory(SnapshotDate.asClock())

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = deviceConfig)

    // TODO Test against ListDetailScaffold

    @Test
    fun eventList() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventListUi(
                    state = EventListState.Success(
                        itemList = Json
                            .upcomingEvents()
                            .map { factory(it, false) }
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
    fun eventHistory() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventHistoryUi(
                    state = EventHistoryState(
                        itemList = Json
                            .upcomingEvents()
                            .map(Event::toEventHistoryStateItem)
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

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun deviceConfigs() = listOf(
            // TODO Include desktop layout
            DeviceConfig.PIXEL_9_PRO_FOLD,
            DeviceConfig.PIXEL_8_PRO,
            DeviceConfig.PIXEL_6A,
        )
    }
}

private fun Event.toEventHistoryStateItem() = EventHistoryState.Item(
    thumbnailImageSeed = thumbnailImageSeed,
    uuid = uuid,
    name = name,
    year = "${dateStart.year}",
)

@Composable
private fun <T : Any> List<T>.toLazyPagingItems(): LazyPagingItems<T> {
    val pagingData = remember(this) { MutableStateFlow(PagingData.from(this)) }
    return pagingData.collectAsLazyPagingItems()
}
