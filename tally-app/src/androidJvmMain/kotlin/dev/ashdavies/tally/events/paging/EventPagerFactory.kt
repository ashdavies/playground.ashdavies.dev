package dev.ashdavies.tally.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.sqldelight.paging3.QueryPagingSource
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.Suspended
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.tally.PlaygroundDatabase
import dev.ashdavies.tally.events.Event
import dev.ashdavies.tally.events.EventsQueries
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

private object EventPagerDefaults {
    const val PAGE_SIZE = 10
}

@OptIn(ExperimentalPagingApi::class)
internal fun eventPager(
    eventsCallable: UpcomingEventsCallable,
    eventsQueries: Suspended<EventsQueries>,
    pageSize: Int = EventPagerDefaults.PAGE_SIZE,
    context: CoroutineContext = Dispatchers.IO,
): Pager<Long, Event> {
    val eventsQueriesBlocking by lazy { runBlocking { eventsQueries() } }
    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        QueryPagingSource<Long, Event>(
            transacter = eventsQueriesBlocking,
            context = context,
            pageBoundariesProvider = { anchor, limit ->
                eventsQueriesBlocking.pageBoundariesAscending(
                    limit = limit,
                    anchor = anchor,
                )
            },
            queryProvider = { beginInclusive, endExclusive ->
                eventsQueriesBlocking.keyedQueryAscending(
                    beginInclusive = beginInclusive,
                    endExclusive = endExclusive,
                )
            },
        )
    }

    return Pager(
        config = PagingConfig(pageSize),
        initialKey = null,
        remoteMediator = EventsRemoteMediator(
            eventsQueries = eventsQueries,
            eventsCallable = eventsCallable,
            onInvalidate = pagingSourceFactory::invalidate,
        ),
        pagingSourceFactory = pagingSourceFactory,
    )
}

@ContributesTo(AppScope::class)
internal interface EventPagerProvider {

    @Provides
    fun eventPager(
        httpClient: HttpClient,
        remoteConfig: RemoteConfig,
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    ): Pager<*, Event> = eventPager(
        eventsCallable = UpcomingEventsCallable(
            httpClient = httpClient,
            remoteConfig = remoteConfig,
        ),
        eventsQueries = databaseFactory.map {
            it.eventsQueries
        },
    )
}
