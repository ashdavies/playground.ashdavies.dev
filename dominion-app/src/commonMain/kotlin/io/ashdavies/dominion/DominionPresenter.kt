package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.dominion.card.CardScreen
import io.ashdavies.dominion.home.HomeScreen
import io.ashdavies.dominion.kingdom.KingdomScreen

@Parcelize
public sealed class DominionScreen : Parcelable, Screen {
    internal data class Kingdom(val expansion: DominionExpansion) : DominionScreen()
    internal data class Card(val card: DominionCard) : DominionScreen()

    public object Home : DominionScreen()
}

internal sealed interface DominionEvent : CircuitUiEvent {
    sealed interface NavEvent : DominionEvent {
        data class GoTo(val screen: DominionScreen) : NavEvent
        object Pop : NavEvent
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

public class DominionPresenterFactory : Presenter.Factory {
    override fun create(screen: Screen, navigator: Navigator, context: CircuitContext): Presenter<*>? = when (screen) {
        is DominionScreen -> presenterOf { DominionPresenter(navigator) }
        else -> null
    }
}

public class DominionUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is DominionScreen.Home -> ui<DominionState> { state, modifier ->
            HomeScreen(
                onClick = { state.sink(DominionEvent.NavEvent.GoTo(DominionScreen.Kingdom(it))) },
                modifier = modifier,
            )
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
            CardScreen(
                onBack = { state.sink(DominionEvent.NavEvent.Pop) },
                modifier = modifier,
                card = screen.card,
            )
        }

        else -> null
    }
}
