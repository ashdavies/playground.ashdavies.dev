package io.ashdavies.playground.events

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import io.ashdavies.http.LegacyEvent
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.apis.EventsApi

private const val NETWORK_PAGE_SIZE = 100

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator(
    private val queries: EventsQueries,
    private val api: EventsApi,
) : RemoteMediator<String, Event>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, Event>,
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.PREPEND -> return endOfPaginationReached()
            LoadType.REFRESH -> null
            LoadType.APPEND ->
                state
                    .lastItemOrNull()?.id
                    ?: return endOfPaginationReached()
        }

        val response = api.getEvents(
            limit = NETWORK_PAGE_SIZE,
            startAt = loadKey,
        ).body()

        queries.transaction {
            if (loadType == LoadType.REFRESH) {
                queries.deleteAll()
            }

            response.forEach {
                queries.insertOrReplace(LegacyEvent(it))
            }
        }

        return MediatorResult.Success(
            endOfPaginationReached = response.isEmpty(),
        )
    }

    private fun endOfPaginationReached(): MediatorResult {
        return MediatorResult.Success(endOfPaginationReached = true)
    }
}
