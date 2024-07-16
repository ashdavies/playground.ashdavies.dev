package io.ashdavies.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import io.ashdavies.events.AndroidMakers
import io.ashdavies.events.DroidconBerlin
import io.ashdavies.events.DroidconLondon
import io.ashdavies.events.Event
import io.ashdavies.events.EventsScreen
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.paging.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

@Preview
@Composable
internal fun ActivityScreenPreview(data: List<Event> = DroidconEvents) {
    EventsScreen(EventsScreen.State(lazyPagingItems(flowOf(PagingData.from(data)))))
}

@Composable
private fun <T : Any> lazyPagingItems(value: Flow<PagingData<T>>): LazyPagingItems<T> {
    return value.collectAsLazyPagingItems()
}
