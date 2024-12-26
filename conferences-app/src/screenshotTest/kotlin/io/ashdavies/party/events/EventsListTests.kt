package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight
import io.ashdavies.party.upcoming.UpcomingEventsPane
import io.ashdavies.party.upcoming.UpcomingEventsScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

internal class EventsListTests {

    @Composable
    @PreviewDayNight
    private fun EventsListPreview(data: List<Event> = DroidconEvents) {
        MaterialPreviewTheme {
            UpcomingEventsPane(
                state = UpcomingEventsScreen.State(lazyPagingItems(flowOf(PagingData.from(data)))),
                onClick = { },
            )
        }
    }

    @Composable
    private fun <T : Any> lazyPagingItems(value: Flow<PagingData<T>>): LazyPagingItems<T> {
        return value.collectAsLazyPagingItems()
    }
}
