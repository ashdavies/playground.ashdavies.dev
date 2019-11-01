package io.ashdavies.playground.conferences

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.ashdavies.playground.github.GitHubDao
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.models.Repo

internal class ConferencesRepository(
    private val service: GitHubService,
    private val dao: GitHubDao
) {

  fun repos(query: String): ConferencesViewState {
    val factory: DataSource.Factory<Int, Repo> = dao.repos("%$query%")
    val callback = ConferencesBoundaryCallback(service, dao, query)

    val config: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(true)
        .setPrefetchDistance(50)
        .build()

    val data: LiveData<PagedList<Repo>> = LivePagedListBuilder(factory, config)
        .setBoundaryCallback(callback)
        .build()

    return ConferencesViewState(data, callback.error)
  }

  companion object {

    private const val PAGE_SIZE = 20
  }
}
