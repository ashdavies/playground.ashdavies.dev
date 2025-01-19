package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight
import io.ashdavies.party.upcoming.UpcomingEventsPane
import io.ashdavies.party.upcoming.UpcomingEventsScreen
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

internal class EventsListTests {

    @Composable
    @PreviewDayNight
    private fun EventsListPreview(data: List<Event> = DroidconEvents) {
        MaterialPreviewTheme {
            UpcomingEventsPane(
                state = UpcomingEventsScreen.State(
                    itemList = data.toImmutableList(),
                    selectedIndex = null,
                    isRefreshing = false,
                    errorMessage = null,
                    eventSink = { },
                ),
                onClick = { },
            )
        }
    }

    @Composable
    private fun <T : Any> lazyPagingItems(value: Flow<PagingData<T>>): LazyPagingItems<T> {
        return value.collectAsLazyPagingItems()
    }
}
