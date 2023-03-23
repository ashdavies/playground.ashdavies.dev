package io.ashdavies.playground.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import io.ashdavies.playground.AndroidMakers
import io.ashdavies.playground.DroidconBerlin
import io.ashdavies.playground.DroidconLondon
import io.ashdavies.playground.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(data: List<Event> = DroidconEvents) {
    HomeScreen(HomeScreen.State(lazyPagingItems(flowOf(PagingData.from(data)))) { })
}

@Composable
private fun <T : Any> lazyPagingItems(value: Flow<PagingData<T>>): LazyPagingItems<T> {
    return value.collectAsLazyPagingItems()
}
