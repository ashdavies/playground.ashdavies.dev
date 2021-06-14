package io.ashdavies.playground.conferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import io.ashdavies.playground.conferences.ConferencesViewState.Failure
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Header
import io.ashdavies.playground.conferences.ConferencesViewState.Section.Item
import io.ashdavies.playground.conferences.ConferencesViewState.Uninitialised
import io.ashdavies.playground.database.Conference
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

private val StoreResponse<*>.errorMessage: String
    get() = errorMessageOrNull() ?: throw IllegalStateException()

internal class ConferencesViewModel(
    store: Store<Unit, List<Conference>>,
) : ViewModel() {

    val viewState: StateFlow<ConferencesViewState> = store
        .stream(StoreRequest.cached(Unit, refresh = true))
        .map { ConferencesViewState(it) }
        .stateIn(viewModelScope, Eagerly, Uninitialised)

    private fun ConferencesViewState(
        response: StoreResponse<List<Conference>>,
    ): ConferencesViewState = when (response) {
        is StoreResponse.Loading -> ConferencesViewState.Loading
        is StoreResponse.Data -> Success(response.value)
        is StoreResponse.Error -> Failure(response.errorMessage)
        else -> Uninitialised
    }

    private fun Success(data: List<Conference>): Success = data
        .map(::Item)
        .groupBy { it.data.dateStart.firstDayOfTheMonth() }
        .flatMap { listOf(Header(it.key)) + it.value }
        .let(ConferencesViewState::Success)
}

private fun String.firstDayOfTheMonth(): LocalDate {
    return LocalDate
        .parse(this)
        .firstDayOfTheMonth()
}

private fun LocalDate.firstDayOfTheMonth(): LocalDate {
    return minus(DatePeriod(days = dayOfMonth))
}

private typealias Success = ConferencesViewState
