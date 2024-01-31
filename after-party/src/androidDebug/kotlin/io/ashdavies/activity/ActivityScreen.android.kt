package io.ashdavies.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import io.ashdavies.events.AndroidMakers
import io.ashdavies.events.DroidconBerlin
import io.ashdavies.events.DroidconLondon
import io.ashdavies.events.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

@Preview
@Composable
internal fun ActivityScreenPreview(data: List<Event> = DroidconEvents) {
    ActivityScreen(ActivityScreen.State(lazyPagingItems(flowOf(PagingData.from(data)))))
}

@Composable
private fun <T : Any> lazyPagingItems(value: Flow<PagingData<T>>): LazyPagingItems<T> {
    return value.collectAsLazyPagingItems()
}
