package io.ashdavies.databinding.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import io.ashdavies.databinding.database.GitHubDao
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.network.Response
import io.ashdavies.databinding.services.GitHubService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class RepoBoundaryCallback(
    private val service: GitHubService,
    private val dao: GitHubDao,
    private val query: String
) : PagedList.BoundaryCallback<Repo>() {

  private val _error: MutableLiveData<Throwable> = MutableLiveData()
  val error: LiveData<Throwable> = _error

  private var page: Int = 1

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
