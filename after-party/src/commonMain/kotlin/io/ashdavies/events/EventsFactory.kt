package io.ashdavies.events

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public fun eventsPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, _, _ ->
    when (screen) {
        is EventsScreen -> presenterOf { EventsPresenter() }
        else -> null
    }
}

public fun eventsUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is EventsScreen -> ui<EventsScreen.State> { state, modifier ->
            EventsScreen(state, modifier)
        }

        else -> null
    }
}
