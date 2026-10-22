package dev.ashdavies.playground.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.ashdavies.playground.event.Event
import dev.ashdavies.playground.event.EventAttrs
import dev.ashdavies.playground.event.EventQueries

@ExperimentalPagingApi
internal class EventsRemoteMediator<T : Any>(
    private val eventQueries: EventQueries,
    private val eventsCallable: UpcomingEventsCallable,
    private val onInvalidate: () -> Unit,
) : RemoteMediator<T, Event>() {

    @Suppress("ReturnCount")
    override suspend fun load(loadType: LoadType, state: PagingState<T, Event>): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.APPEND -> state.lastItemOrNull() ?: return endOfPaginationReached()
            LoadType.PREPEND -> return endOfPaginationReached()
            LoadType.REFRESH -> null
        }

        return eventsCallable(GetEventsRequest(loadKey?.dateStart))
            .mapCatching { list ->
                eventQueries.transaction { list.forEach { eventQueries.upsert(it.toDatabaseEvent()) } }
                onInvalidate()
                list
            }
            .fold(
                onSuccess = { MediatorResult.Success(it.size < state.config.pageSize) },
                onFailure = { MediatorResult.Error(it) },
            )
    }
}

@ExperimentalPagingApi
private fun endOfPaginationReached() = RemoteMediator.MediatorResult.Success(
    endOfPaginationReached = true,
)

private fun DomainWrapper<DomainEvent>.toDatabaseEvent() = with(value) {
    Event(
        uuid = uuid,
        name = name,
        website = website,
        location = location,
        dateStart = dateStart,
        dateEnd = dateEnd,
        attrs = EventAttrs(attrs),
    )
}
