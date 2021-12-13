package io.ashdavies.playground.events

import androidx.lifecycle.ViewModel
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.events.EventsViewState.Failure
import io.ashdavies.playground.events.EventsViewState.Uninitialised
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

@OptIn(ExperimentalCoroutinesApi::class)
internal class EventsViewModel(store: Store<Unit, List<Event>>) : ViewModel() {

    val viewState: Flow<EventsViewState> = store
        .stream(StoreRequest.cached(Unit, true))
        .map { EventsViewState(it) }

    private fun EventsViewState(response: StoreResponse<List<Event>>): EventsViewState = when (response) {
        is StoreResponse.Loading -> EventsViewState.Loading
        is StoreResponse.Error -> Failure(response.errorMessage)
        is StoreResponse.Data -> Success(response.value)
        else -> Uninitialised
    }

    private fun Success(data: List<Event>): EventsViewState.Success = data
        .map { EventsViewState.Section(it.name, it.location, it.dateStart) }
        .sortedByDescending { it.date }
        .let(EventsViewState::Success)
}
