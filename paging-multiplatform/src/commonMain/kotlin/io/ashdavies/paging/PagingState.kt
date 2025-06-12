package io.ashdavies.paging

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope

@Composable
public expect fun <T : Any> rememberPagingState(
    retainedCoroutineScope: CoroutineScope,
    pager: Pager<*, T>,
): PagingState<T>

public interface PagingState<T> {
    val itemList: ImmutableList<T>
    val isRefreshing: Boolean
    val errorMessage: String?
    fun refresh()
}
