package dev.ashdavies.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope

public actual class Pager<K : Any, V : Any>

@Composable
public actual fun <T : Any> rememberPagingState(
    retainedCoroutineScope: CoroutineScope,
    pager: Pager<*, T>,
): PagingState<T> = remember(pager) {
    object : PagingState<T> {
        override val itemList = persistentListOf<T>()
        override val isRefreshing = false
        override val errorMessage = null
        override fun refresh() = Unit
    }
}
