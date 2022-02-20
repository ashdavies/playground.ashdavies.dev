package io.ashdavies.playground.common

import androidx.paging.PagingState

internal fun <Key : Any, Value : Any> PagingState<Key, Value>.findRefreshKey(): Key? {
    val closestPage = closestPageToPosition(anchorPosition ?: return null)
    return closestPage?.prevKey ?: closestPage?.nextKey
}

internal fun <T : Any, R> PagingState<*, T>.lastItemOrNull(block: (T) -> R): R? = lastItemOrNull()?.let(block)
