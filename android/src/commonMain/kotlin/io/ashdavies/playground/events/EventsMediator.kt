package io.ashdavies.playground.events

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.ashdavies.playground.common.findRefreshKey
import io.ashdavies.playground.common.lastItemOrNull
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.network.EventsService
import io.ashdavies.playground.network.todayAsString
import io.ktor.client.features.ResponseException
import io.ktor.client.plugins.ResponseException

@ExperimentalPagingApi
internal class EventsMediator(
    private val queries: EventsQueries,
    private val service: EventsService,
) : RemoteMediator<String, Event>() {

    /**
     * PREPEND is not currently supported as we do not know the previous key request, we could store the requests
     * as they arrive similar to the RemoteKeys approach from the Google CodeLabs example.
     *
     * @see <a href="https://github.com/googlecodelabs/android-paging/blob/step13-19_network_and_database/app/src/main/java/com/example/android/codelabs/paging/db/RemoteKeys.kt">RemoteKeys.kt</a>
     */
    override suspend fun load(loadType: LoadType, state: PagingState<String, Event>): MediatorResult {
        println("EventsMediator.load(loadType = $loadType, state.pages.size = ${state.pages.size}")
        val startAt: String = when (loadType) {
            LoadType.APPEND -> state.lastItemOrNull { it.dateStart } ?: return MediatorResult.Success(true)
            LoadType.PREPEND -> return MediatorResult.Success(false)
            LoadType.REFRESH -> state.findRefreshKey() ?: todayAsString()
        }

        return try {
            val data: List<Event> = service.get(startAt, state.config.pageSize)
            val nextKey: String? = data
                .takeIf { it.size >= state.config.pageSize }
                ?.lastOrNull()
                ?.dateStart

            if (loadType == LoadType.REFRESH) {
                queries.deleteAll()
            }

            data.forEach(queries::insertOrReplace)
            MediatorResult.Success(endOfPaginationReached = nextKey == null)
        } catch (exception: ResponseException) {
            MediatorResult.Error(exception)
        }
    }
}
