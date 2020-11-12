package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.Conference
import kotlinx.datetime.LocalDate

internal sealed class ConferencesViewState {

    object Uninitialised : ConferencesViewState()

    object Loading : ConferencesViewState()

    data class Success(val data: List<Section>) : ConferencesViewState()

    data class Failure(val message: String) : ConferencesViewState()

    sealed class Section {

        data class Header(val date: LocalDate) : Section()

        data class Item(val data: Conference) : Section()
    }
}
