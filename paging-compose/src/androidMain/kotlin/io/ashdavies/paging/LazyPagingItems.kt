package io.ashdavies.paging

import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow

public actual typealias LazyPagingItems<T> = androidx.paging.compose.LazyPagingItems<T>

@Composable
public actual fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(): LazyPagingItems<T> {
    return collectAsLazyPagingItems()
}
