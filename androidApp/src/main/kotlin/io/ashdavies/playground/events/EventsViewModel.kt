package io.ashdavies.playground.events

import androidx.lifecycle.ViewModel
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest.Companion.cached
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.events.EventsViewState.Failure
import io.ashdavies.playground.events.EventsViewState.Uninitialised
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
internal class EventsViewModel(private val store: Store<Unit, List<Event>>) : ViewModel() {

    private val _onRefresh = Channel<Unit>()
    private val onRefresh = _onRefresh.receiveAsFlow()

    val viewState: Flow<EventsViewState> = onRefresh
        .onStart { emit(Unit) }
        .flatMapLatest { store.stream(cached(Unit, true)) }
        .map(::EventsViewState)

    fun onRefresh() {
        _onRefresh.trySend(Unit)
    }

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
