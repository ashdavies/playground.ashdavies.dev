package io.ashdavies.dominion

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize

public fun dominionScreen(): Screen = DominionScreen.Expansions

internal sealed interface DominionScreen : Parcelable, Screen {

    @Parcelize
    data object Expansions : DominionScreen {
        data class State(
            val expansions: List<Expansion>,
            val isLoading: Boolean,
            val eventSink: (Event) -> Unit,
        ) : CircuitUiState

        sealed interface Event : CircuitUiEvent {
            data class ShowExpansion(
                val expansion: Expansion,
            ) : Event
        }
    }

    @Parcelize
    data class Kingdom(val expansion: String) : DominionScreen {
        data class State(
            val expansion: String,
            val cards: List<Card>,
            val expandedCard: Card?,
            val eventSink: (Event) -> Unit,
        ) : CircuitUiState

        sealed interface Event : CircuitUiEvent {
            data class ExpandCard(
                val card: Card,
            ) : Event

            data object Back : Event
        }
    }
}
