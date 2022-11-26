package io.ashdavies.paging

import androidx.compose.runtime.Composable
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow

public expect class LazyPagingItems<T : Any>

@Composable
public expect fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(): LazyPagingItems<T>
