package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.paging.collectAsLazyPagingItems
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

internal class EventsListTest {

    @Composable
    @PreviewDayNight
    fun EventsListPreview(data: List<Event> = DroidconEvents) {
        MaterialPreviewTheme {
            EventsList(
                state = EventsScreen.State(lazyPagingItems(flowOf(PagingData.from(data)))),
                onClick = { },
            )
        }
    }

    @Composable
    private fun <T : Any> lazyPagingItems(value: Flow<PagingData<T>>): LazyPagingItems<T> {
        return value.collectAsLazyPagingItems()
    }
}
