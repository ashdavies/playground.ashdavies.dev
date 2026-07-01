package dev.ashdavies.playground.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.ashdavies.http.common.models.ApiConference
import dev.ashdavies.playground.event.Event
import dev.ashdavies.playground.event.EventQueries

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator<T : Any>(
    private val eventsQueries: EventQueries,
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

        return eventsCallable(GetEventsRequest(loadKey?.dateStart)).fold(
            onSuccess = { MediatorResult.Success(eventsQueries.insertOrIgnoreAll(it) == 0L).also { onInvalidate() } },
            onFailure = { MediatorResult.Error(it) },
        )
    }
}

private suspend fun EventQueries.insertOrIgnoreAll(items: List<ApiConference>): Long {
    var rowsInserted = 0L

    transaction {
        items.forEach {
            rowsInserted += insertOrIgnore(
                name = it.name,
                website = it.website,
                location = it.location,
                imageUrl = it.imageUrl,
                status = it.status,
                online = it.online,
                dateStart = it.dateStart,
                dateEnd = it.dateEnd,
                cfpStart = it.cfp?.start,
                cfpEnd = it.cfp?.end,
                cfpSite = it.cfp?.site,
            )
        }
    }

    return rowsInserted
}

@ExperimentalPagingApi
private fun endOfPaginationReached(): RemoteMediator.MediatorResult {
    return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
}
