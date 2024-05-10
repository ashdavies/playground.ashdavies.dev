package io.ashdavies.dominion

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize

public fun dominionScreen(): Screen = DominionScreen.BoxSetList

internal sealed interface DominionScreen : Parcelable, Screen {

    @Parcelize
    data object BoxSetList : DominionScreen {
        data class State(
            val boxSetList: List<BoxSet>,
            val isLoading: Boolean,
            val eventSink: (Event) -> Unit,
        ) : CircuitUiState

        sealed interface Event : CircuitUiEvent {
            data class ShowBoxSet(
                val boxSet: BoxSet,
            ) : Event
        }
    }

    @Parcelize
    data class BoxSetDetails(val title: String) : DominionScreen {
        data class State(
            val boxSet: BoxSet,
            val cards: List<Card>,
            val expandedCard: Card?,
            val isLoading: Boolean,
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
