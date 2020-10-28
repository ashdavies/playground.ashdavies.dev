package io.ashdavies.playground.conferences

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewModelScope
import io.ashdavies.playground.lifecycle.ViewModelFactory
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class ConferencesViewModel(
    conferencesNameParser: ConferencesNameParser,
    conferencesRepository: ConferencesRepository,
) : ViewModel() {

    val viewState: StateFlow<List<Conference>> =
        conferencesRepository
            .getAll()
            .map { conferencesNameParser(it) }
            .stateIn(viewModelScope, Eagerly, emptyList())

    companion object {

        @Suppress("FunctionName")
        fun Factory(context: Context): Factory = ViewModelFactory(context) {
            ConferencesViewModel(
                conferencesNameParser = ConferencesNameParser(),
                conferencesRepository = conferencesRepository,
            )
        }
    }
}
