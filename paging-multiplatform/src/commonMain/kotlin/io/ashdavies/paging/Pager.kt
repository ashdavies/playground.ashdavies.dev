package io.ashdavies.paging

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope

public expect class Pager<Key : Any, Value : Any>

public interface PagingState<T> {

    val itemList: ImmutableList<T?>
    val isRefreshing: Boolean
    val errorMessage: String?
    fun refresh()

    companion object {

        fun <T> empty() = object : PagingState<T> {
            override val itemList = persistentListOf<T?>()
            override val isRefreshing = false
            override val errorMessage = null
            override fun refresh() = Unit
        }
    }
}

@Composable
public expect fun <T : Any> rememberPagingState(
    retainedCoroutineScope: CoroutineScope,
    pager: Pager<*, T>,
): PagingState<T>
