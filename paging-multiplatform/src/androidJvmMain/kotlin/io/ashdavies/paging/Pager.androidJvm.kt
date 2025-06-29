package io.ashdavies.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope

public actual typealias Pager<K, V> = androidx.paging.Pager<K, V>

@Composable
public actual fun <T : Any> rememberPagingState(
    retainedCoroutineScope: CoroutineScope,
    pager: Pager<*, T>,
): PagingState<T> {
    val pagingItems = remember(retainedCoroutineScope) {
        pager.flow.cachedIn(retainedCoroutineScope)
    }.collectAsLazyPagingItems()

    return PagingState(pagingItems)
}

private fun <T : Any> PagingState(pagingItems: LazyPagingItems<T>): PagingState<T> = object : PagingState<T> {
    override val itemList = pagingItems.itemSnapshotList.toImmutableList()
    override val isRefreshing = pagingItems.loadState.refresh is LoadState.Loading
    override val errorMessage = pagingItems.loadState.errorMessage
    override fun refresh() = pagingItems.refresh()
}

private val CombinedLoadStates.errorMessage: String?
    get() = listOfNotNull(source, mediator)
        .firstNotNullOfOrNull { loadStates ->
            listOf(
                loadStates.refresh,
                loadStates.prepend,
                loadStates.append,
            ).firstNotNullOfOrNull {
                it as? LoadState.Error
            }
        }
        ?.error
        ?.message
