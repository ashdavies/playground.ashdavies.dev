package io.ashdavies.dominion

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public fun dominionPresenterFactory(): Presenter.Factory {
    return Presenter.Factory { screen, navigator, _ ->
        when (screen) {
            is DominionScreen.Expansions -> presenterOf { ExpansionsPresenter(navigator) }
            is DominionScreen.Kingdom -> presenterOf { KingdomPresenter(screen.expansion) }
            else -> null
        }
    }
}

public fun dominionUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is DominionScreen.Expansions -> ui<DominionScreen.Expansions.State> { state, modifier ->
            ExpansionsScreen(state, modifier)
        }

        is DominionScreen.Kingdom -> ui<DominionScreen.Kingdom.State> { state, modifier ->
            KingdomScreen(state, modifier)
        }

        else -> null
    }
}