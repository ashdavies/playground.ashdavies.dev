package dev.ashdavies.playground.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.sqldelight.paging3.QueryPagingSource
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.paging.PagerFactory
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.playground.event.Event
import dev.ashdavies.playground.event.EventQueries
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.Suspended
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

@ContributesBinding(AppScope::class)
internal class EventPagerFactory(
    private val eventsCallable: UpcomingEventsCallable,
    private val eventsQueries: Suspended<EventQueries>,
    private val coroutineContext: CoroutineContext,
) : PagerFactory<Long, Event> {

    @Inject constructor(
        httpClient: HttpClient,
        remoteConfig: RemoteConfig,
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    ) : this(
        eventsCallable = UpcomingEventsCallable(httpClient, remoteConfig),
        eventsQueries = databaseFactory.map { it.eventQueries },
        coroutineContext = Dispatchers.IO,
    )

    override fun create(config: PagerConfig<Long>): Pager<Long, Event> {
        val eventsQueriesBlocking by lazy { runBlocking { eventsQueries() } }
        val pagingSourceFactory = InvalidatingPagingSourceFactory {
            QueryPagingSource<Long, Event>(
                transacter = eventsQueriesBlocking,
                context = coroutineContext,
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

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(config.pageSize),
            initialKey = config.initialKey,
            remoteMediator = EventsRemoteMediator(
                eventsQueries = eventsQueries,
                eventsCallable = eventsCallable,
                onInvalidate = pagingSourceFactory::invalidate,
            ),
            pagingSourceFactory = pagingSourceFactory,
        )
    }
}
