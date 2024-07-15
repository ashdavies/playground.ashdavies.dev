package io.ashdavies.events

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.activity.ActivityPresenter
import io.ashdavies.activity.ActivityScreen

public fun eventsPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, _, _ ->
    when (screen) {
        is ActivityScreen -> presenterOf { ActivityPresenter() }
        else -> null
    }
}

public fun eventsUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is ActivityScreen -> ui<ActivityScreen.State> { state, modifier ->
            ActivityScreen(state, modifier)
        }

        else -> null
    }
}
