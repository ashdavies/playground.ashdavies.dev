package io.ashdavies.databinding.repos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.ashdavies.databinding.database.GitHubDao
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.services.GitHubService

internal class RepoRepository(
    private val service: GitHubService,
    private val dao: GitHubDao
) {

  fun repos(query: String): Pair<LiveData<PagedList<Repo>>, LiveData<Throwable>> {
    val factory: DataSource.Factory<Int, Repo> = dao.repos("%$query%")
    val callback = RepoBoundaryCallback(service, dao, query)

    val data: LiveData<PagedList<Repo>> = LivePagedListBuilder(factory, DATABASE_PAGE_SIZE)
        .setBoundaryCallback(callback)
        .build()

    return data to callback.error
  }

  companion object {

    private const val DATABASE_PAGE_SIZE = 20
  }
}
