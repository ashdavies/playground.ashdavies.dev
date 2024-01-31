package io.ashdavies.events

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.activity.ActivityPresenter
import io.ashdavies.activity.ActivityScreen
import io.ashdavies.profile.ProfilePresenter
import io.ashdavies.profile.ProfileScreen

public fun EventsPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, _, _ ->
    when (screen) {
        is ActivityScreen -> presenterOf { ActivityPresenter() }
        is ProfileScreen -> presenterOf { ProfilePresenter() }
        else -> null
    }
}

public fun EventsUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is ActivityScreen -> ui<ActivityScreen.State> { state, modifier -> ActivityScreen(state, modifier) }
        is ProfileScreen -> ui<ProfileScreen.State> { state, modifier -> ProfileScreen(state, modifier) }
        else -> null
    }
}
