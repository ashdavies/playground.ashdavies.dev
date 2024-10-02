package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.paging.collectAsLazyPagingItems
import io.ashdavies.party.tooling.PreviewDayNight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

@Composable
@PreviewDayNight
internal fun EventsScreenPreview(data: List<Event> = DroidconEvents) {
    EventsScreen(EventsScreen.State(lazyPagingItems(flowOf(PagingData.from(data)))))
}

@Composable
private fun <T : Any> lazyPagingItems(value: Flow<PagingData<T>>): LazyPagingItems<T> {
    return value.collectAsLazyPagingItems()
}
