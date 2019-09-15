package io.ashdavies.playground.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import io.ashdavies.architecture.Event
import io.ashdavies.extensions.map
import io.ashdavies.extensions.switchMap
import io.ashdavies.playground.github.GitHubDatabase
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.models.Repo
import io.ashdavies.playground.mutableLiveData
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

  private val _query: Channel<String> = Channel()
  private val query: MutableLiveData<String> = mutableLiveData()

  private val result: LiveData<RepoViewState> = query.map(repository::repos)

  val items: LiveData<PagedList<Repo>> = result.switchMap { it.data }
  val errors: LiveData<Event<Throwable>> = result
      .switchMap { it.errors }
      .map(::Event)

  init {
    viewModelScope.launch {
      _query
          .consumeAsFlow()
          .filter { it.length >= MIN_LENGTH }
          .debounce(500)
          .collect { query.value = it }
    }
  }

  fun onQuery(value: String) {
    viewModelScope.launch {
      _query.send(value)
    }
  }

  class Factory(
      private val context: Context
  ) : ViewModelProvider.Factory {

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
