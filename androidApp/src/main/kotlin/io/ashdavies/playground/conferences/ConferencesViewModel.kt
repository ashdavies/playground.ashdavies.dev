@file:Suppress("FunctionName")

package io.ashdavies.playground.conferences

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Header
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Item
import io.ashdavies.playground.conferences.ConferencesViewState.Uninitialised
import io.ashdavies.playground.lifecycle.ViewModelFactory
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

internal class ConferencesViewModel(store: Store<Any, List<Conference>>) : ViewModel() {

    val viewState: StateFlow<ConferencesViewState> =
        store
            .stream(StoreRequest.cached(Unit, refresh = true))
            .map { ConferencesViewState(it) }
            .stateIn(viewModelScope, Eagerly, Uninitialised)

    private fun ConferencesViewState(response: StoreResponse<List<Conference>>): ConferencesViewState =
        when (response) {
            is StoreResponse.Loading -> ConferencesViewState.Loading
            is StoreResponse.Data -> ConferencesViewState(response.value)
            is StoreResponse.Error -> ConferencesViewState.Failure(response.errorMessage)
            else -> Uninitialised
        }

    private fun ConferencesViewState(data: List<Conference>): ConferencesViewState =
        data
            .map(::Item)
            .groupBy { it.data.dateStart }
            .flatMap { listOf(Header(it.key)) + it.value }
            .let(ConferencesViewState::Success)

    companion object {

        fun Factory(context: Context): Factory = ViewModelFactory(context) {
            ConferencesViewModel(conferencesStore)
        }
    }
}
