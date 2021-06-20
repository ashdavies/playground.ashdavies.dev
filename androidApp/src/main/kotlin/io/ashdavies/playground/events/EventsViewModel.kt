package io.ashdavies.playground.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.events.EventsViewState.Failure
import io.ashdavies.playground.events.EventsViewState.Section.Header
import io.ashdavies.playground.events.EventsViewState.Section.Item
import io.ashdavies.playground.events.EventsViewState.Uninitialised
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

internal class EventsViewModel(
    store: Store<Unit, List<Event>>,
) : ViewModel() {

    val viewState: StateFlow<EventsViewState> = store
        .stream(StoreRequest.cached(Unit, refresh = true))
        .map { EventsViewState(it) }
        .stateIn(viewModelScope, Eagerly, Uninitialised)

    private fun EventsViewState(
        response: StoreResponse<List<Event>>,
    ): EventsViewState = when (response) {
        is StoreResponse.Loading -> EventsViewState.Loading
        is StoreResponse.Data -> Success(response.value)
        is StoreResponse.Error -> Failure(response.errorMessage)
        else -> Uninitialised
    }

    private fun Success(data: List<Event>): Success = data
        .map(::Item)
        .groupBy { it.data.dateStart.firstDayOfTheMonth() }
        .flatMap { listOf(Header(it.key)) + it.value }
        .let(EventsViewState::Success)
}

private fun String.firstDayOfTheMonth(): LocalDate {
    return LocalDate
        .parse(this)
        .firstDayOfTheMonth()
}

private fun LocalDate.firstDayOfTheMonth(): LocalDate {
    return minus(DatePeriod(days = dayOfMonth))
}

private typealias Success = EventsViewState
