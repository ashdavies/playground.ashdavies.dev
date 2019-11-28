package io.ashdavies.playground.conferences

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import io.ashdavies.extensions.liveData
import io.ashdavies.extensions.switchMap
import io.ashdavies.playground.github.GitHubDatabase
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.models.Repo
import io.ashdavies.playground.navigation.ChannelNavDirector
import io.ashdavies.playground.navigation.NavDirector
import retrofit2.create

internal class ConferencesViewModel(
    repository: ConferencesRepository
) : NavDirector by ChannelNavDirector(),
    ViewModel() {

  private val result: LiveData<ConferencesViewState> = liveData { repository.repos("kotlin") }

  val items: LiveData<PagedList<Repo>> = result.switchMap(ConferencesViewState::data)
  val errors: LiveData<Throwable> = result.switchMap(ConferencesViewState::errors)

  class Factory(
      private val context: Context
  ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(kls: Class<T>): T {
      val database: GitHubDatabase = database(context)
      val service: GitHubService = retrofit.create()

      val repository = ConferencesRepository(service, database.repo())
      return ConferencesViewModel(repository) as T
    }
  }
}
