package io.ashdavies.playground.events

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.events.EventsViewState.Failure
import io.ashdavies.playground.events.EventsViewState.Section.Header
import io.ashdavies.playground.events.EventsViewState.Section.Item
import io.ashdavies.playground.events.EventsViewState.Uninitialised
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

internal class EventsViewModel(private val provider: suspend () -> Store<Unit, List<Event>>) {

    val viewState: Flow<EventsViewState> = flow { emit(provider()) }
        .flatMapLatest { it.stream(StoreRequest.cached(Unit, true)) }
        .map { EventsViewState(it) }

    private fun EventsViewState(response: StoreResponse<List<Event>>): EventsViewState =
        when (response) {
            is StoreResponse.Loading -> EventsViewState.Loading
            is StoreResponse.Error -> Failure(response.errorMessage)
            is StoreResponse.Data -> Success(response.value)
            else -> Uninitialised
        }

    private fun Success(data: List<Event>): EventsViewState.Success = data
        .map(::Item)
        .groupBy { it.data.dateStart.firstDayOfTheMonth() }
        .flatMap { listOf(Header(it.key)) + it.value }
        .let(EventsViewState::Success)

    private fun String.firstDayOfTheMonth() = LocalDate
        .parse(this)
        .firstDayOfTheMonth()

    private fun LocalDate.firstDayOfTheMonth(): LocalDate {
        return minus(DatePeriod(days = dayOfMonth))
    }
}
