package io.ashdavies.playground.events

import io.ashdavies.playground.database.Event
import kotlinx.datetime.LocalDate

internal sealed class EventsViewState {

    object Uninitialised : EventsViewState()
    object Loading : EventsViewState()

    data class Success(val data: List<Section>) : EventsViewState()
    data class Failure(val message: String) : EventsViewState()

    sealed class Section {

        data class Header(val date: LocalDate) : Section()
        data class Item(val data: Event) : Section()
    }
}
