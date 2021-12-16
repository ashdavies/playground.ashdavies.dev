package io.ashdavies.playground.events

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.squareup.sqldelight.Query
import io.ashdavies.playground.common.findRefreshKey
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.database.EventsQueries
import io.ashdavies.playground.network.todayAsString
import kotlin.properties.Delegates.observable

internal class EventsSource(private val queries: EventsQueries) : PagingSource<String, Event>(), Query.Listener {

    private var currentQuery: Query<Event>? by observable(null) { _, oldValue, newValue ->
        oldValue?.removeListener(this)
        newValue?.addListener(this)
    }

    init {
        registerInvalidatedCallback {
            currentQuery?.removeListener(this)
            currentQuery = null
        }
    }

    override fun getRefreshKey(state: PagingState<String, Event>): String? {
        return state.findRefreshKey()
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Event> = try {
        println("EventsSource.load(params = $params)")
        val startAt = params.key ?: todayAsString()

        val data: List<Event> = queries
            .selectAll(startAt, params.loadSize.toLong())
            .also { currentQuery = it }
            .executeAsList()

        val nextKey = data
            .takeIf { it.size >= params.loadSize }
            ?.lastOrNull()
            ?.dateStart

        LoadResult.Page(
            nextKey = nextKey,
            itemsBefore = 0,
            itemsAfter = 4,
            prevKey = null,
            data = data,
        )
    } catch (exception: Exception) {
        //LoadResult.Error(exception)
        throw exception
    }

    override fun queryResultsChanged() {
        invalidate()
    }
}
