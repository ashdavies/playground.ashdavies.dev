package io.ashdavies.playground.events

import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.android.ViewModel
import io.ashdavies.playground.android.viewModelScope
import io.ashdavies.playground.kotlin.CloseableFlow
import io.ashdavies.playground.kotlin.asCloseableFlow
import io.ashdavies.playground.network.todayAsString

private const val NETWORK_PAGE_SIZE = 10

internal class EventsViewModel(private val queries: EventsQueries, private val service: EventsService) : ViewModel() {

    private val pager = Pager(
        config = PagingConfig(NETWORK_PAGE_SIZE),
        clientScope = viewModelScope,
        initialKey = todayAsString(),
        getItems = ::getItems,
    )

    val pagingData: CloseableFlow<PagingData<Event>>
        get() = pager.pagingData
            .cachedIn(viewModelScope)
            .asCloseableFlow()

    private suspend fun getItems(currentKey: String, pageSize: Int): PagingResult<String, Event> {
        suspend fun fetchItems(currentKey: String, pageSize: Int): List<Event> {
            val items: List<Event> = service.events(currentKey, pageSize)
            queries.transaction { items.forEach(queries::insertOrReplace) }
            return items
        }

        val items: List<Event> = queries
            .selectAll(currentKey, pageSize.toLong())
            .executeAsList()
            .takeUnless { it.isEmpty() }
            ?: fetchItems(currentKey, pageSize)

        val nextKey = items
            .takeIf { it.size >= pageSize }
            ?.lastOrNull()
            ?.dateStart

        return PagingResult(
            currentKey = currentKey,
            nextKey = { nextKey },
            prevKey = { null },
            items = items,
        )
    }
}
