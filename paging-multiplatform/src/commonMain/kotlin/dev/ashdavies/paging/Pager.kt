package dev.ashdavies.paging

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope

public expect class Pager<Key : Any, Value : Any>

public interface PagingState<T> {
    public val itemList: ImmutableList<T?>
    public val isRefreshing: Boolean
    public val errorMessage: String?
    public fun refresh()
}

@Composable
public expect fun <T : Any> rememberPagingState(
    retainedCoroutineScope: CoroutineScope,
    pager: Pager<*, T>,
): PagingState<T>
