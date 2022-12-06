package io.ashdavies.paging

import androidx.compose.runtime.Composable
import androidx.paging.LOGGER
import androidx.paging.Logger
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow

public actual typealias LazyPagingItems<T> = androidx.paging.compose.LazyPagingItems<T>

@Composable
public actual fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(): LazyPagingItems<T> {
    LOGGER = StandardLogger; return collectAsLazyPagingItems()
}

private object StandardLogger : Logger {
    override fun isLoggable(level: Int): Boolean = true
    override fun log(level: Int, message: String, tr: Throwable?) {
        println("$level: $message")
        tr?.printStackTrace()
    }
}
