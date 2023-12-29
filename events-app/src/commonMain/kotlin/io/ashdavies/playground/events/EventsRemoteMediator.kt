package io.ashdavies.playground.events

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import io.ashdavies.http.DatabaseEvent
import io.ashdavies.playground.EventsQueries
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.get
import io.ashdavies.http.common.models.Event as ApiEvent
import io.ashdavies.playground.Event as DatabaseEvent

private const val PLAYGROUND_API_HOST = "playground.ashdavies.dev"
private const val NETWORK_PAGE_SIZE = 100

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator(
    private val eventsQueries: EventsQueries,
    private val httpClient: HttpClient,
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

        val query = buildList {
            if (loadKey != null) add("startAt=$loadKey")
            add("limit=$NETWORK_PAGE_SIZE")
        }

        val result: List<ApiEvent> = try {
            httpClient.get("https://$PLAYGROUND_API_HOST/events?${query.joinToString("&")}")
        } catch (exception: SocketTimeoutException) {
            return MediatorResult.Error(exception)
        }.body()

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
