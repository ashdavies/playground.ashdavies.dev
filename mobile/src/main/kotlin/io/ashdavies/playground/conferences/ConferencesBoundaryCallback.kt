package io.ashdavies.playground.conferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import io.ashdavies.playground.github.GitHubDao
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.models.Repo
import io.ashdavies.playground.network.Response
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class ConferencesBoundaryCallback(
    private val service: GitHubService,
    private val dao: GitHubDao,
    private val query: String
) : PagedList.BoundaryCallback<Repo>() {

  private val _error: MutableLiveData<Throwable> = MutableLiveData()
  val error: LiveData<Throwable> = _error

  private var page: Int = 0

  override fun onZeroItemsLoaded() {
    requestItems()
  }

  override fun onItemAtEndLoaded(itemAtEnd: Repo) {
    requestItems()
  }

  private fun requestItems() {
    GlobalScope.launch {
      val result: Result<Response<Repo>> = runCatching {
        service.repos("$query+in:name,description", page, NETWORK_PAGE_SIZE)
      }

      result.onSuccess {
        dao.insert(it.items)
        page++
      }

      result.onFailure {
        _error.postValue(it)
      }
    }
  }

  companion object {

    private const val NETWORK_PAGE_SIZE = 50
  }
}
