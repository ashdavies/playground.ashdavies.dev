package io.ashdavies.party.events

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf

public fun eventsPresenterFactory(): Presenter.Factory = presenterFactoryOf<EventsScreen> { _, _ ->
    EventsPresenter()
}

public fun eventsUiFactory(): Ui.Factory {
    return uiFactoryOf<EventsScreen, EventsScreen.State> { _, state, modifier ->
        EventsScreen(state, modifier)
    }
}
