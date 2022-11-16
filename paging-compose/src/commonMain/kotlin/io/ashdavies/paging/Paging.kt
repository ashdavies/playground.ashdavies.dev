@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package io.ashdavies.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow

public expect val <T : Any> LazyPagingItems<T>.errorMessage: String?

public expect val <T : Any> LazyPagingItems<T>.isRefreshing: Boolean

public expect val <T : Any> LazyPagingItems<T>.itemCount: Int

public expect operator fun <T : Any> LazyPagingItems<T>.get(index: Int): T?

public expect fun <T : Any> LazyPagingItems<T>.refresh()

public expect fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
)
