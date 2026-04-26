package dev.ashdavies.paging

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope

public data class PagerConfig<Key : Any>(
    val initialKey: Key,
    val pageSize: Int,
)

public expect class Pager<Key : Any, Value : Any>

public interface PagingState<Value> {
    public val itemList: ImmutableList<Value?>
    public val isRefreshing: Boolean
    public val errorMessage: String?
    public fun refresh()
}

public interface PagerFactory<Key : Any, Value : Any> {
    public fun create(config: PagerConfig<Key>): Pager<Key, Value>
}

@Composable
public expect fun <T : Any> rememberPagingState(
    retainedCoroutineScope: CoroutineScope,
    pager: Pager<*, T>,
): PagingState<T>
