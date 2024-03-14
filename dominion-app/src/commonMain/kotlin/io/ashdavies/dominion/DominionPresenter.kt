package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize

public fun dominionScreen(): Screen = DominionScreen.Home

@Parcelize
internal sealed interface DominionScreen : Parcelable, Screen {
    data class Kingdom(val expansion: DominionExpansion) : DominionScreen
    data class Card(val card: DominionCard) : DominionScreen

    object Home : DominionScreen
}

internal sealed interface DominionEvent : CircuitUiEvent {
    sealed interface NavEvent : DominionEvent {
        data class GoTo(val screen: DominionScreen) : NavEvent
        data object Pop : NavEvent
    }
}

internal data class DominionState(
    val sink: (DominionEvent) -> Unit,
) : CircuitUiState

@Composable
internal fun DominionPresenter(navigator: Navigator): DominionState = DominionState { event ->
    when (event) {
        is DominionEvent.NavEvent.GoTo -> navigator.goTo(event.screen)
        is DominionEvent.NavEvent.Pop -> navigator.pop()
    }
}
