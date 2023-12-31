package io.ashdavies.party

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public fun AfterPartyPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    if (screen is AfterPartyScreen) presenterOf { AfterPartyPresenter(navigator) } else null
}

public fun AfterPartyUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    if (screen is AfterPartyScreen) {
        ui<AfterPartyScreen.State> { state, modifier ->
            AfterPartyScreen(state, modifier)
        }
    } else {
        null
    }
}
