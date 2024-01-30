package io.ashdavies.playground.events

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import io.ashdavies.http.DatabaseEvent
import io.ashdavies.playground.EventsQueries
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ashdavies.http.common.models.Event as ApiEvent
import io.ashdavies.playground.Event as DatabaseEvent

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
                eventsQueries.insertOrReplace(DatabaseEvent(it))
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
