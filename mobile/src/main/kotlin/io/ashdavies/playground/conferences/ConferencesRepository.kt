package io.ashdavies.playground.conferences

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.ashdavies.playground.github.ConferenceDao
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.CoroutineScope

internal class ConferencesRepository(
    private val dao: ConferenceDao,
    private val service: ConferencesService
) {

  fun conferences(scope: CoroutineScope): ConferencesViewState {
    val factory: DataSource.Factory<Int, Conference> = dao.conferences()
    val callback = ConferencesBoundaryCallback(dao, scope, service)

    val config: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(true)
        .setPrefetchDistance(50)
        .build()

    val data: LiveData<PagedList<Conference>> = LivePagedListBuilder(factory, config)
        .setBoundaryCallback(callback)
        .build()

    return ConferencesViewState(data, callback.error)
  }

  companion object {

    private const val PAGE_SIZE = 20
  }
}
