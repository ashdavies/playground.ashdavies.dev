package io.ashdavies.playground.conferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import io.ashdavies.playground.github.ConferenceDao
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class ConferencesBoundaryCallback(
    private val dao: ConferenceDao,
    private val scope: CoroutineScope,
    private val service: ConferencesService
) : PagedList.BoundaryCallback<Conference>() {

    private val _error: MutableLiveData<Throwable> = MutableLiveData()
    val error: LiveData<Throwable> = _error

    private var page: Int = 0

    override fun onZeroItemsLoaded() {
        requestItems()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Conference) {
        requestItems()
    }

    private fun requestItems() {
        scope.launch {
            val result: Result<List<Conference>> = runCatching {
                service.conferences(page, NETWORK_PAGE_SIZE)
            }

            result.onSuccess {
                dao.insert(it)
                page++
            }

            result.onFailure {
                _error.postValue(it)
            }
        }
    }

    companion object {

        private const val NETWORK_PAGE_SIZE = 50L
    }
}
