package io.ashdavies.playground.conferences

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import io.ashdavies.playground.navigation.NavDirectionsStore
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow

@ExperimentalCoroutinesApi
internal class ConferencesViewModel(
    private val repository: ConferencesRepository
) : NavDirectionsStore, ViewModel() {

    private val _navDirections: Channel<NavDirections> = Channel(CONFLATED)
    override val navDirections: Flow<NavDirections> get() = _navDirections.receiveAsFlow()

    val items: Flow<List<Conference>> get() = repository
        .getAll()
        .catch { onError(it) }

    private suspend fun onError(throwable: Throwable) {
        _navDirections.send(ConferencesFragmentDirections.navigateToErrorDialog("Error", "An unknown error has occured"))
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {

        @FlowPreview
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(kls: Class<T>): T {
            return ConferencesViewModel(context.conferencesRepository) as T
        }
    }
}
