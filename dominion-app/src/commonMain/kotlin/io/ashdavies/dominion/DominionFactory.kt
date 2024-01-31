package io.ashdavies.dominion

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.dominion.card.CardScreen
import io.ashdavies.dominion.home.HomeScreen
import io.ashdavies.dominion.kingdom.KingdomScreen

public fun DominionPresenterFactory(): Presenter.Factory {
    return presenterFactoryOf<DominionScreen> { _, navigator ->
        DominionPresenter(navigator)
    }
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
