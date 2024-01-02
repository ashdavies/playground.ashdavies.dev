package io.ashdavies.playground

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.playground.activity.ActivityPresenter
import io.ashdavies.playground.activity.ActivityScreen
import io.ashdavies.playground.profile.ProfilePresenter
import io.ashdavies.playground.profile.ProfileScreen

public fun EventsPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
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
