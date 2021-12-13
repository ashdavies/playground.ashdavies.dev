package io.ashdavies.playground.events

internal sealed class EventsViewState {

    object Uninitialised : EventsViewState()
    object Loading : EventsViewState()

    data class Success(val data: List<Section>) : EventsViewState()
    data class Failure(val message: String) : EventsViewState()

    data class Section(
        val name: String,
        val location: String,
        val date: String,
    ) : EventsViewState()
}
