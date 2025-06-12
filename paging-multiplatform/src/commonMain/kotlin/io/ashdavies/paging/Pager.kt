package io.ashdavies.paging

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope

public expect class Pager<Key : Any, Value : Any>

public interface PagingState<T> {
    val itemList: ImmutableList<T?>
    val isRefreshing: Boolean
    val errorMessage: String?
    fun refresh()
}

@Composable
public expect fun <T : Any> rememberPagingState(
    retainedCoroutineScope: CoroutineScope,
    pager: Pager<*, T>,
): PagingState<T>
