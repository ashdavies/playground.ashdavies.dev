package io.ashdavies.playground.conferences

import androidx.lifecycle.ViewModel
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.conferences.ConferencesViewState.Failure
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Item
import io.ashdavies.playground.conferences.ConferencesViewState.Uninitialised
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.emptyString
import io.ashdavies.playground.network.ConferencesService
import kotlinx.coroutines.flow.Flow

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

internal class ConferencesViewModel(
    conferencesService: ConferencesService,
    /*store: Store<Any, List<Conference>>*/
) : ViewModel() {

    val viewState: Flow<ConferencesViewState> = TODO()

    /*val viewState: StateFlow<ConferencesViewState> =
        store
            .stream(StoreRequest.cached(Unit, refresh = true))
            .map { ConferencesViewState(it) }
            .stateIn(viewModelScope, Eagerly, Uninitialised)*/

    private fun ConferencesViewState(result: Result<List<Conference>>): ConferencesViewState =
        result.fold(
            onFailure = { Failure(it.message ?: emptyString()) },
            onSuccess = { Success(it) }

        )

    private fun ConferencesViewState(response: StoreResponse<List<Conference>>): ConferencesViewState =
        when (response) {
            is StoreResponse.Loading -> ConferencesViewState.Loading
            is StoreResponse.Data -> Success(response.value)
            is StoreResponse.Error -> Failure(response.errorMessage)
            else -> Uninitialised
        }

    private fun Success(data: List<Conference>): Success =
        data
            .map(::Item)
            //.groupBy { it.data.dateStart }
            //.flatMap { listOf(Header(it.key)) + it.value }
            .let(ConferencesViewState::Success)

}

private typealias Success = ConferencesViewState
