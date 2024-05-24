package io.ashdavies.events

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
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
    ): MediatorResult = when (loadType) {
        LoadType.PREPEND -> endOfPaginationReached()
        else -> when (val lastItem = state.lastItemOrNull()) {
            is DatabaseEvent -> load(loadType, lastItem.id)
            else -> endOfPaginationReached()
        }
    }

    private suspend fun load(
        loadType: LoadType,
        startAt: String?,
    ): MediatorResult = when (val result = eventsCallable.result(GetEventsRequest(startAt))) {
        is CallableResult.Error<*> -> MediatorResult.Error(result.throwable)
        is CallableResult.Success -> {
            eventsQueries.transaction {
                if (loadType == LoadType.REFRESH) eventsQueries.deleteAll()

                result.value.forEach {
                    eventsQueries.insertOrReplace(it.asDatabaseEvent())
                }
            }

            MediatorResult.Success(result.value.isEmpty())
        }
    }

    private fun endOfPaginationReached(): MediatorResult {
        return MediatorResult.Success(endOfPaginationReached = true)
    }
}

private suspend fun GetEventsCallable.result(
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
    status = status, online = online, dateStart = dateStart,
    dateEnd = dateEnd, cfpStart = cfp?.start, cfpEnd = cfp?.end,
    cfpSite = cfp?.site,
)
