package io.ashdavies.playground.android

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.CombinedLoadStates
import androidx.paging.DifferCallback
import androidx.paging.ItemSnapshotList
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.NullPaddedList
import androidx.paging.PagingDataDiffer
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.swing.Swing

private val IncompleteLoadState = LoadState.NotLoading(false)
private val InitialLoadStates = LoadStates(
    refresh = IncompleteLoadState,
    prepend = IncompleteLoadState,
    append = IncompleteLoadState,
)

@Composable
public actual fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(): LazyPagingItems<T> {
    val lazyPagingItems = remember(this) { LazyPagingItems(this) }
    LaunchedEffect(lazyPagingItems) { lazyPagingItems.collectPagingData() }
    LaunchedEffect(lazyPagingItems) { lazyPagingItems.collectLoadState() }
    return lazyPagingItems
}

public actual class LazyPagingItems<T : Any>(private val flow: Flow<PagingData<T>>) {

    private val pagingDataDiffer = object : PagingDataDiffer<T>(
        differCallback = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) = if (count > 0) updateItemSnapshotList() else Unit
            override fun onInserted(position: Int, count: Int) = if (count > 0) updateItemSnapshotList() else Unit
            override fun onRemoved(position: Int, count: Int) = if (count > 0) updateItemSnapshotList() else Unit
        },
        mainDispatcher = Dispatchers.Swing,
    ) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            onListPresentable()
            updateItemSnapshotList()
            return null
        }
    }

    internal var itemSnapshotList by mutableStateOf(ItemSnapshotList<T>(0, 0, emptyList()))
        private set

    internal var loadState: CombinedLoadStates by mutableStateOf(
        CombinedLoadStates(
            IncompleteLoadState,
            IncompleteLoadState,
            IncompleteLoadState,
            InitialLoadStates
        )
    )
        private set

    internal suspend fun collectLoadState() =
        pagingDataDiffer.loadStateFlow.collect { loadState = it }

    internal suspend fun collectPagingData() =
        flow.collectLatest { pagingDataDiffer.collectFrom(it) }

    internal operator fun get(index: Int): T? {
        pagingDataDiffer[index] // this registers the value load
        return itemSnapshotList[index]
    }

    internal fun refresh() =
        pagingDataDiffer.refresh()

    private fun updateItemSnapshotList() {
        itemSnapshotList = pagingDataDiffer.snapshot()
    }
}

public actual val <T : Any> LazyPagingItems<T>.errorMessage: String?
    get() = (loadState.append as? LoadState.Error)
        ?.error
        ?.message

public actual val <T : Any> LazyPagingItems<T>.isRefreshing: Boolean
    get() = loadState.append is LoadState.Loading

public actual val <T : Any> LazyPagingItems<T>.itemCount: Int
    get() = itemSnapshotList.size

public actual operator fun <T : Any> LazyPagingItems<T>.get(index: Int): T? =
    get(index)

public actual fun <T : Any> LazyPagingItems<T>.refresh() =
    refresh()

public actual fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(count = items.itemCount) { index ->
        itemContent(items[index])
    }
}
