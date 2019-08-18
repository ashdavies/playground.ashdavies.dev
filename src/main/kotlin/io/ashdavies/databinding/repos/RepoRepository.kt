package io.ashdavies.databinding.repos

import io.ashdavies.databinding.database.GitHubDao
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.network.Response
import io.ashdavies.databinding.services.GitHubService

internal class RepoRepository(
    private val service: GitHubService,
    private val dao: GitHubDao
) {

  private var page: Int = 1
  private val size: Int = 50

  suspend fun repos(query: String): List<Repo> {
    val result: Result<Response<Repo>> = runCatching {
      service.repos("$query+in:name,description", page, size)
    }

    result.onSuccess {
      dao.insert(it.items)
      page++
    }

    result.onFailure {
      TODO()
    }

    return dao.repos("%$query%")
  }
}
