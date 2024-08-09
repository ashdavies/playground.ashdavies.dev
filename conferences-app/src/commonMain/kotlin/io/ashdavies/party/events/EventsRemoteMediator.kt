package io.ashdavies.party.events

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ashdavies.http.common.models.Event as ApiEvent
import io.ashdavies.party.events.Event as DatabaseEvent

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator(
    private val eventsQueries: EventsQueries,
    private val eventsCallable: GetUpcomingEventsCallable,
    private val onInvalidate: () -> Unit,
) : RemoteMediator<String, DatabaseEvent>() {

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, DatabaseEvent>,
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.APPEND -> state.lastItemOrNull() ?: return endOfPaginationReached()
            LoadType.PREPEND -> return endOfPaginationReached()
            LoadType.REFRESH -> null
        }

        return when (val result = eventsCallable.result(GetEventsRequest(loadKey?.dateStart))) {
            is CallableResult.Error<*> -> MediatorResult.Error(result.throwable)
            is CallableResult.Success -> {
                eventsQueries.transaction {
                    if (loadType == LoadType.REFRESH) eventsQueries.deleteAll()

                    result.value.forEach {
                        eventsQueries.insertOrReplace(it.asDatabaseEvent())
                    }
                }

                onInvalidate()

                MediatorResult.Success(result.value.isEmpty())
            }
        }
    }
}

@ExperimentalPagingApi
private fun endOfPaginationReached(): RemoteMediator.MediatorResult {
    return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
}

private suspend fun GetUpcomingEventsCallable.result(
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

private fun ApiEvent.asDatabaseEvent(): DatabaseEvent = DatabaseEvent(
    id = id, name = name, website = website, location = location,
    imageUrl = imageUrl, status = status, online = online,
    dateStart = dateStart, dateEnd = dateEnd, cfpStart = cfp?.start,
    cfpEnd = cfp?.end, cfpSite = cfp?.site,
)
