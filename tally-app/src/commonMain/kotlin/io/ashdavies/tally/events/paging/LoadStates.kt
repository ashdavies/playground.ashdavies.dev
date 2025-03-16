package io.ashdavies.tally.events.paging

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates

private val CombinedLoadStates.list: List<LoadStates>
    get() = listOfNotNull(source, mediator)

private val LoadStates.list: List<LoadState>
    get() = listOf(refresh, prepend, append)

internal val CombinedLoadStates.errorMessage: String?
    get() = firstNotNullOfOrNull { it as? LoadState.Error }?.error?.message

internal val CombinedLoadStates.isRefreshing: Boolean
    get() = refresh is LoadState.Loading

private fun <T> CombinedLoadStates.firstNotNullOfOrNull(predicate: (LoadState) -> T): T? {
    return list.firstNotNullOfOrNull { it.list.firstNotNullOfOrNull(predicate) }
}
