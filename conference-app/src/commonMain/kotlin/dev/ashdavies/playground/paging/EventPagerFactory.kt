package dev.ashdavies.playground.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.sqldelight.paging3.QueryPagingSource
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.paging.PagerFactory
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.playground.event.Event
import dev.ashdavies.playground.metro.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SuspendLazy
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@ExperimentalMetroCoroutinesApi
@ContributesBinding(AppScope::class)
internal class EventPagerFactory(
    private val databaseFactory: SuspendLazy<PlaygroundDatabase>,
    private val eventsCallable: UpcomingEventsCallable,
    private val coroutineContext: CoroutineContext,
) : PagerFactory<Int, Event> {

    @Inject constructor(
        databaseFactory: SuspendLazy<PlaygroundDatabase>,
        eventsCallable: UpcomingEventsCallable,
    ) : this(
        databaseFactory = databaseFactory,
        eventsCallable = eventsCallable,
        coroutineContext = Dispatchers.Main,
    )

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun create(config: PagerConfig<Int>): Pager<Int, Event> = databaseFactory.map { database ->
        val eventQueries = database.eventQueries

        val pagingSourceFactory = InvalidatingPagingSourceFactory {
            QueryPagingSource(
                countQuery = eventQueries.countAll(),
                transacter = eventQueries,
                context = coroutineContext,
                queryProvider = { limit, offset ->
                    eventQueries.getAllDescending(
                        limit = limit,
                        offset = offset,
                    )
                },
            )
        }

        Pager(
            config = PagingConfig(config.pageSize),
            initialKey = config.initialKey,
            remoteMediator = EventsRemoteMediator(
                eventQueries = eventQueries,
                attrQueries = database.attrQueries,
                eventsCallable = eventsCallable,
                onInvalidate = pagingSourceFactory::invalidate,
            ),
            pagingSourceFactory = pagingSourceFactory::invoke,
        )
    }.await()
}
