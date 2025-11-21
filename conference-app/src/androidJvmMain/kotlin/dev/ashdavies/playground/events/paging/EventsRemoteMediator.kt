package dev.ashdavies.playground.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.ashdavies.playground.events.Event
import dev.ashdavies.playground.events.EventsQueries
import dev.ashdavies.sql.Suspended
import io.ktor.client.network.sockets.SocketTimeoutException
import dev.ashdavies.http.common.models.ApiConference as ApiEvent

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator<T : Any>(
    private val eventsQueries: Suspended<EventsQueries>,
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

        return when (val result = eventsCallable.result(GetEventsRequest(loadKey?.dateStart))) {
            is CallableResult.Error<*> -> MediatorResult.Error(result.throwable)

            is CallableResult.Success -> {
                val eventsQueries = eventsQueries()
                var rowsInserted = 0L

                eventsQueries.transaction {
                    result.value.forEach {
                        rowsInserted += eventsQueries.insertOrIgnore(
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

                onInvalidate()

                MediatorResult.Success(rowsInserted == 0L)
            }
        }
    }
}

@ExperimentalPagingApi
private fun endOfPaginationReached(): RemoteMediator.MediatorResult {
    return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
}

private suspend fun UpcomingEventsCallable.result(
    request: GetEventsRequest,
): CallableResult<List<ApiEvent>> = try {
    CallableResult.Success(invoke(request))
} catch (exception: SocketTimeoutException) {
    CallableResult.Error(exception)
} catch (exception: GetEventsError) {
    CallableResult.Error(exception)
}

private sealed interface CallableResult<out T> {
    data class Error<out T>(val throwable: Throwable) : CallableResult<T>
    data class Success<out T>(val value: T) : CallableResult<T>
}
