package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference

internal sealed class ConferencesViewState {

    object Uninitialised : ConferencesViewState()

    object Loading : ConferencesViewState()

    data class Success(val data: List<Section>) : ConferencesViewState()

    data class Failure(val message: String) : ConferencesViewState()

    sealed class Section {

        data class Header(val timeInMillis: Long) : Section()

        data class Item(val data: Conference) : Section()
    }
}
