package io.ashdavies.playground.conferences

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import io.ashdavies.extensions.liveData
import io.ashdavies.extensions.switchMap
import io.ashdavies.playground.github.ConferenceDatabase
import io.ashdavies.playground.navigation.ChannelNavDirector
import io.ashdavies.playground.navigation.NavDirector
import io.ashdavies.playground.network.Conference

internal class ConferencesViewModel(
    repository: ConferencesRepository
) : NavDirector by ChannelNavDirector(),
    ViewModel() {

    private val result: LiveData<ConferencesViewState> =
        liveData { repository.conferences(viewModelScope) }

    val items: LiveData<PagedList<Conference>> = result.switchMap(ConferencesViewState::data)
    val errors: LiveData<Throwable> = result.switchMap(ConferencesViewState::errors)

    class Factory(
        private val context: Context
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(kls: Class<T>): T {
            val database: ConferenceDatabase = database(context)
            val service: ConferencesService = service()

            val repository = ConferencesRepository(database.dao(), service)
            return ConferencesViewModel(repository) as T
        }
    }
}
