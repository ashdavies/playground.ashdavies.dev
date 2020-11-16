package io.ashdavies.playground.conferences

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Item
import io.ashdavies.playground.conferences.ConferencesViewState.Uninitialised
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.lifecycle.ViewModelFactory
import io.ashdavies.playground.network.ConferencesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

internal class ConferencesViewModel(
    conferencesService: ConferencesService,
    conferencesMapper: ConferencesMapper,
    /*store: Store<Any, List<Conference>>*/
) : ViewModel() {

    val viewState: Flow<ConferencesViewState> = flow {
        val conferences: List<ConferencesViewState.Section> = conferencesService
            .getAll()
            .documents
            .map { conferencesMapper(it.content) }
            .map(::Item)

        emit(ConferencesViewState.Success(conferences))
    }

    /*val viewState: StateFlow<ConferencesViewState> =
        store
            .stream(StoreRequest.cached(Unit, refresh = true))
            .map { ConferencesViewState(it) }
            .stateIn(viewModelScope, Eagerly, Uninitialised)*/

    private fun ConferencesViewState(response: StoreResponse<List<Conference>>): ConferencesViewState =
        when (response) {
            is StoreResponse.Loading -> ConferencesViewState.Loading
            is StoreResponse.Data -> Success(response.value)
            is StoreResponse.Error -> ConferencesViewState.Failure(response.errorMessage)
            else -> Uninitialised
        }

    private fun Success(data: List<Conference>): ConferencesViewState =
        data
            .map(::Item)
            //.groupBy { it.data.dateStart }
            //.flatMap { listOf(Header(it.key)) + it.value }
            .let(ConferencesViewState::Success)

    companion object {

        fun Factory(context: Context): Factory = ViewModelFactory(context) {
            ConferencesViewModel(conferencesService, ConferencesMapper())
        }
    }
}
