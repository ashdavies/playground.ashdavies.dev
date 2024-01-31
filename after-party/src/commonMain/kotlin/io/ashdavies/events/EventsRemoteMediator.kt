package io.ashdavies.events

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ashdavies.events.Event as DatabaseEvent
import io.ashdavies.http.common.models.Event as ApiEvent

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator(
    private val eventsQueries: EventsQueries,
    private val eventsCallable: GetEventsCallable,
) : RemoteMediator<String, DatabaseEvent>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, DatabaseEvent>,
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.APPEND -> state.lastItemOrNull()?.id ?: return endOfPaginationReached()
            LoadType.PREPEND -> return endOfPaginationReached()
            LoadType.REFRESH -> null
        }

        val result: List<ApiEvent> = try {
            eventsCallable(GetEventsRequest(loadKey))
        } catch (exception: SocketTimeoutException) {
            return MediatorResult.Error(exception)
        } catch (exception: GetEventsError) {
            return MediatorResult.Error(exception)
        }

        eventsQueries.transaction {
            if (loadType == LoadType.REFRESH) {
                eventsQueries.deleteAll()
            }

            result.forEach {
                eventsQueries.insertOrReplace(it.asDatabaseEvent())
            }
        }

        return MediatorResult.Success(
            endOfPaginationReached = result.isEmpty(),
        )
    }

    private fun endOfPaginationReached(): MediatorResult {
        return MediatorResult.Success(endOfPaginationReached = true)
    }
}

private fun ApiEvent.asDatabaseEvent(): DatabaseEvent = DatabaseEvent(
    id = id, name = name, website = website, location = location,
    status = status, online = online, dateStart = dateStart,
    dateEnd = dateEnd, cfpStart = cfp?.start, cfpEnd = cfp?.end,
    cfpSite = cfp?.site,
)
