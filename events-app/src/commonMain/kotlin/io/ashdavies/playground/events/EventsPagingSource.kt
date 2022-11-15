package io.ashdavies.playground.events

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.network.todayAsString

internal class EventsPagingSource(
    private val queries: EventsQueries,
) : PagingSource<String, Event>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Event> {
        val query = queries.selectAll(
            startAt = params.key ?: todayAsString(),
            limit = params.loadSize.toLong(),
        ).executeAsList()

        return LoadResult.Page(
            nextKey = query.last().dateStart,
            prevKey = null,
            data = query,
        )
    }

    override fun getRefreshKey(state: PagingState<String, Event>): String? = null
}
