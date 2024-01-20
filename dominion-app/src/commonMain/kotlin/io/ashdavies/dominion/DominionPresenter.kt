package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.dominion.card.CardScreen
import io.ashdavies.dominion.home.HomeScreen
import io.ashdavies.dominion.kingdom.KingdomScreen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize

@Parcelize
public sealed class DominionScreen : Parcelable, Screen {
    internal data class Kingdom(val expansion: DominionExpansion) : DominionScreen()
    internal data class Card(val card: DominionCard) : DominionScreen()

    public object Home : DominionScreen()
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

public fun DominionPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    if (screen is DominionScreen) presenterOf { DominionPresenter(navigator) } else null
}

public fun DominionUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is DominionScreen.Home -> ui<DominionState> { state, modifier ->
            HomeScreen(modifier = modifier) {
                state.sink(DominionEvent.NavEvent.GoTo(DominionScreen.Kingdom(it)))
            }
        }

        is DominionScreen.Kingdom -> ui<DominionState> { state, modifier ->
            KingdomScreen(
                onClick = { state.sink(DominionEvent.NavEvent.GoTo(DominionScreen.Card(it))) },
                onBack = { state.sink(DominionEvent.NavEvent.Pop) },
                expansion = screen.expansion,
                modifier = modifier,
            )
        }

        is DominionScreen.Card -> ui<DominionState> { state, modifier ->
            CardScreen(screen.card, modifier) { state.sink(DominionEvent.NavEvent.Pop) }
        }

        else -> null
    }
}
