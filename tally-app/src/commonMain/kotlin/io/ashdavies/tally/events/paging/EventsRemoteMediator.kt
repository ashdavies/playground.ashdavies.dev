package io.ashdavies.tally.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.EventsQueries
import io.ashdavies.tally.events.callable.GetEventsError
import io.ashdavies.tally.events.callable.GetEventsRequest
import io.ashdavies.tally.events.callable.PagedUpcomingEventsCallable
import io.ktor.client.network.sockets.*
import io.ashdavies.http.common.models.Event as ApiEvent

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator(
    private val eventsQueries: EventsQueries,
    private val eventsCallable: PagedUpcomingEventsCallable,
    private val onInvalidate: () -> Unit,
) : RemoteMediator<Long, Event>() {

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, Event>,
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
                    if (loadType == LoadType.REFRESH) {
                        eventsQueries.deleteAll()
                    }

                    result.value.forEach {
                        eventsQueries.insertOrReplace(
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

                MediatorResult.Success(result.value.isEmpty())
            }
        }
    }
}

@ExperimentalPagingApi
private fun endOfPaginationReached(): RemoteMediator.MediatorResult {
    return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
}

private suspend fun PagedUpcomingEventsCallable.result(
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
