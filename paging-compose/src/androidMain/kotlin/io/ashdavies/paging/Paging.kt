@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package io.ashdavies.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.items

public actual val <T : Any> LazyPagingItems<T>.errorMessage: String?
    get() = (loadState.append as? LoadState.Error)
        ?.error
        ?.message

public actual val <T : Any> LazyPagingItems<T>.isRefreshing: Boolean
    get() = loadState.refresh is LoadState.Loading

public actual val <T : Any> LazyPagingItems<T>.itemCount: Int
    get() = itemCount

public actual fun <T : Any> LazyPagingItems<T>.refresh() =
    refresh()

public actual operator fun <T : Any> LazyPagingItems<T>.get(index: Int): T? =
    get(index)

public actual fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
) = items(items, itemContent = itemContent)
