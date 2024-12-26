package io.ashdavies.party.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

internal fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    contentType: ((item: T) -> Any?)? = null,
    itemContent: @Composable LazyItemScope.(item: T?) -> Unit,
) = items(
    count = items.itemCount,
    key = items.itemKey(key),
    contentType = items.itemContentType(contentType),
    itemContent = { itemContent(items[it]) },
)
