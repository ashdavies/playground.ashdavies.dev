package io.ashdavies.playground.conferences

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.paging.PagedList
import io.ashdavies.extensions.liveData
import io.ashdavies.extensions.switchMap
import io.ashdavies.playground.github.ConferenceDatabase
import io.ashdavies.playground.navigation.NavDirectionsStore
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@ExperimentalCoroutinesApi
internal class ConferencesViewModel(
    repository: ConferencesRepository
) : NavDirectionsStore, ViewModel() {

    private val result: LiveData<ConferencesViewState> = liveData {
        repository.conferences(viewModelScope)
    }

    private val _navDirections: Channel<NavDirections> = Channel(CONFLATED)
    override val navDirections: Flow<NavDirections> get() = _navDirections.receiveAsFlow()

    val items: Flow<PagedList<Conference>> = result
        .switchMap(ConferencesViewState::data)
        .asFlow()

    val errors: Flow<Throwable> = result
        .switchMap(ConferencesViewState::errors)
        .asFlow()

    class Factory(private val context: Context) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(kls: Class<T>): T {
            val database: ConferenceDatabase = database(context)
            val service: ConferencesService = service()

            val repository = ConferencesRepository(database.dao(), service)
            return ConferencesViewModel(repository) as T
        }
    }
}
