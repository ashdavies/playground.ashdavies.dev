package io.ashdavies.playground.android

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.flow.Flow

@Composable
public actual fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(): LazyPagingItems<T> {
    return LazyPagingItems(this)
}

public actual class LazyPagingItems<T : Any>(
    private val source: Flow<PagingData<T>>
)

public actual val <T : Any> LazyPagingItems<T>.errorMessage: String?
    get() = null

public actual val <T : Any> LazyPagingItems<T>.isRefreshing: Boolean
    get() = false

public actual val <T : Any> LazyPagingItems<T>.itemCount: Int
    get() = 0

public actual operator fun <T : Any> LazyPagingItems<T>.get(index: Int): T? =
    null

public actual fun <T : Any> LazyPagingItems<T>.refresh() =
    Unit

public actual fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(count = items.itemCount) { index ->
        itemContent(items[index])
    }
}
