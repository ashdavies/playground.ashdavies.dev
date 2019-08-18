package io.ashdavies.databinding.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ashdavies.architecture.Event
import io.ashdavies.databinding.database.GitHubDatabase
import io.ashdavies.databinding.extensions.mutableLiveData
import io.ashdavies.databinding.models.Repo
import io.ashdavies.databinding.services.GitHubService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import retrofit2.create

@FlowPreview
internal class RepoViewModel(repository: RepoRepository) : ViewModel() {

  private val query = Channel<String>()

  private val _items: MutableLiveData<List<Repo>> = mutableLiveData()
  val items: LiveData<List<Repo>> = _items

  private val _loading: MutableLiveData<Boolean> = mutableLiveData()
  val loading: LiveData<Boolean> = _loading

  private val _error: MutableLiveData<Event<Throwable>> = mutableLiveData()
  val error: LiveData<Event<Throwable>> = _error

  init {
    viewModelScope.launch {
      query
          .consumeAsFlow()
          .filter { it.length >= MIN_LENGTH }
          .debounce(500)
          .collect { query ->
            _loading.value = true

            runCatching { repository.repos(query) }
                .onSuccess { _items.value = it }
                .onFailure { _error.value = Event(it) }

            _loading.value = false
          }
    }
  }

  fun onQuery(value: String) {
    viewModelScope.launch { query.send(value) }
  }

  class Factory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(kls: Class<T>): T {
      val database: GitHubDatabase = database(context)
      val service: GitHubService = retrofit.create()

      val repository = RepoRepository(service, database.repo())

      return RepoViewModel(repository) as T
    }
  }

  companion object {

    private const val MIN_LENGTH = 3
  }
}
