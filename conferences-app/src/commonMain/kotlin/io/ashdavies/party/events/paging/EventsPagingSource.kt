package io.ashdavies.party.events.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ashdavies.party.events.Event
import io.ashdavies.party.events.EventsQueries
import io.ashdavies.party.network.todayAsString

internal class EventsPagingSource(private val queries: EventsQueries) : PagingSource<String, Event>() {

    private var lastItem: Event? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Event> {
        val result = queries.selectAllStartingAtAscending(
            startAt = params.key ?: todayAsString(),
            limit = params.loadSize.toLong(),
        ).executeAsList()

        val query = when (result.firstOrNull()) {
            lastItem -> result.drop(1)
            else -> result
        }

        lastItem = query.lastOrNull()

        return LoadResult.Page(
            nextKey = lastItem?.dateStart,
            prevKey = null,
            data = query,
        )
    }

    override fun getRefreshKey(state: PagingState<String, Event>): String? = null
}
