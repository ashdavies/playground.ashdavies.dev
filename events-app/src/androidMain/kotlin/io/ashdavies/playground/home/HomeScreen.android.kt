package io.ashdavies.playground.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.PagingData
import io.ashdavies.playground.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeScreen(pagingData: Flow<PagingData<Event>> = emptyFlow()) {
    HomeScreen(HomeScreen.State(pagingData.collectAsLazyPagingItems()) { })
}
