package io.ashdavies.databinding.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ashdavies.databinding.common.SingleLiveData
import io.ashdavies.databinding.extensions.create
import io.ashdavies.databinding.extensions.mutableLiveDataOf
import io.ashdavies.databinding.extensions.singleLiveDataOf
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.services.GitHub
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

internal class RepoViewModel(service: GitHub) : ViewModel() {

  private val jobs = Job()
  private val query = Channel<String>()

  val items: MutableLiveData<List<Repo>> = mutableLiveDataOf()
  val loading: MutableLiveData<Boolean> = mutableLiveDataOf()
  val error: SingleLiveData<Throwable> = singleLiveDataOf()

  val empty: LiveData<Boolean> = EmptyLiveData(items, loading)

  init {
    launch(UI, parent = jobs) {
      query
          .filter { it.length >= MIN_LENGTH }
          .debounce()
          .consumeEach {
            try {
              items.value = service.getRepos(it).await()
            } catch (throwable: Throwable) {
              error.value = throwable
            } finally {
              loading.value = false
            }
          }
    }
  }

  fun onQuery(value: String): Boolean {
    launch(UI, parent = jobs) { query.send(value) }
    loading.value = true
    return true
  }

  class Factory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(kls: Class<T>): T {
      return RepoViewModel(retrofit.create()) as T
    }
  }

  companion object {

    private const val MIN_LENGTH = 3
  }
}

internal fun <T> ReceiveChannel<T>.debounce(timeout: Long = 500, context: CoroutineContext = DefaultDispatcher): ReceiveChannel<T> = produce(context) {

  var last: Job? = null

  consumeEach {
    last?.cancel()
    last = launch {
      delay(timeout)
      send(it)
    }
  }

  last?.join()
}
