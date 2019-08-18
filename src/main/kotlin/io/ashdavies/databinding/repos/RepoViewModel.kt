package io.ashdavies.databinding.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ashdavies.architecture.Event
import io.ashdavies.databinding.extensions.mutableLiveData
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.services.GitHub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.filter
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.create

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal class RepoViewModel(service: GitHub) : ViewModel() {

  private val query = Channel<CharSequence>()

  private val _items: MutableLiveData<List<Repo>> = mutableLiveData()
  val items: LiveData<List<Repo>> = _items

  private val _loading: MutableLiveData<Boolean> = mutableLiveData()
  val loading: LiveData<Boolean> = _loading

  private val _error: MutableLiveData<Event<Throwable>> = mutableLiveData()
  val error: LiveData<Event<Throwable>> = _error

  val empty: LiveData<Boolean> = EmptyLiveData(items, loading)

  init {
    viewModelScope.launch {
      query
          .filter { it.length >= MIN_LENGTH }
          .debounce(this)
          .consumeEach { query ->
            _loading.value = true

            runCatching { service.getRepos(query) }
                .onSuccess { _items.value = it }
                .onFailure { _error.value = Event(it) }

            _loading.value = false
          }
    }
  }

  fun onQuery(value: CharSequence) {
    viewModelScope.launch { query.send(value) }
  }

  @Suppress("UNCHECKED_CAST")
  class Factory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(kls: Class<T>): T = RepoViewModel(retrofit.create()) as T
  }

  companion object {

    private const val MIN_LENGTH = 3
  }
}

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal fun <T> ReceiveChannel<T>.debounce(scope: CoroutineScope, timeout: Long = 500): ReceiveChannel<T> = scope.produce {
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
