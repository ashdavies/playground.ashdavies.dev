package io.ashdavies.playground.events

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries

private const val NETWORK_PAGE_SIZE = 100

@OptIn(ExperimentalPagingApi::class)
internal class EventsRemoteMediator(
    private val service: EventsService,
    private val queries: EventsQueries,
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

        val response = service.fetchItems(
            limit = NETWORK_PAGE_SIZE,
            startAt = loadKey,
        )

        queries.transaction {
            if (loadType == LoadType.REFRESH) {
                queries.deleteAll()
            }

            response.forEach {
                queries.insertOrReplace(it)
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