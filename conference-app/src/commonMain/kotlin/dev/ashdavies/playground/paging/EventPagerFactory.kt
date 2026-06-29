package dev.ashdavies.playground.paging

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
        coroutineContext = Dispatchers.Main,
    )

    override suspend fun create(config: PagerConfig<Long>): Pager<Long, Event> {
        val eventsQueries = eventsQueries()

        val pagingSourceFactory = InvalidatingPagingSourceFactory {
            QueryPagingSource<Long, Event>(
                transacter = eventsQueries,
                context = coroutineContext,
                pageBoundariesProvider = { anchor, limit ->
                    eventsQueries.pageBoundariesAscending(
                        limit = limit,
                        anchor = anchor,
                    )
                },
                queryProvider = { beginInclusive, endExclusive ->
                    eventsQueries.keyedQueryAscending(
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
            pagingSourceFactory = pagingSourceFactory::invoke,
        )
    }
}
