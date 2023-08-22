package io.ashdavies.playground.events

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import io.ashdavies.generated.apis.EventsApi
import io.ashdavies.http.LegacyEvent
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ktor.client.network.sockets.SocketTimeoutException

private const val NETWORK_PAGE_SIZE = 100

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator(
    private val eventsQueries: EventsQueries,
    private val eventsApi: EventsApi,
) : RemoteMediator<String, Event>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, Event>,
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.APPEND -> state.lastItemOrNull()?.id ?: return endOfPaginationReached()
            LoadType.PREPEND -> return endOfPaginationReached()
            LoadType.REFRESH -> null
        }

        val result = try {
            eventsApi.getEvents(loadKey, limit = NETWORK_PAGE_SIZE)
        } catch (exception: SocketTimeoutException) {
            return MediatorResult.Error(exception)
        }.body()

        eventsQueries.transaction {
            if (loadType == LoadType.REFRESH) {
                eventsQueries.deleteAll()
            }

            result.forEach {
                eventsQueries.insertOrReplace(LegacyEvent(it))
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
