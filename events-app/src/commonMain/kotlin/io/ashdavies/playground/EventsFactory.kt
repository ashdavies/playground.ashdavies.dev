package io.ashdavies.playground

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.playground.activity.ActivityPresenter
import io.ashdavies.playground.activity.ActivityScreen
import io.ashdavies.playground.details.DetailsPresenter
import io.ashdavies.playground.details.DetailsScreen
import io.ashdavies.playground.home.HomePresenter
import io.ashdavies.playground.home.HomeScreen
import io.ashdavies.playground.profile.ProfilePresenter
import io.ashdavies.playground.profile.ProfileScreen

public fun EventsPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is HomeScreen -> presenterOf { HomePresenter(navigator) }
        is ActivityScreen -> presenterOf { ActivityPresenter(navigator) }
        is ProfileScreen -> presenterOf { ProfilePresenter(navigator) }
        is DetailsScreen -> presenterOf { DetailsPresenter(navigator, screen.eventId) }
        else -> null
    }
}

public fun EventsUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is HomeScreen -> ui<HomeScreen.State> { state, modifier -> HomeScreen(state, modifier) }
        is ActivityScreen -> ui<ActivityScreen.State> { state, modifier -> ActivityScreen(state, modifier) }
        is ProfileScreen -> ui<ProfileScreen.State> { state, modifier -> ProfileScreen(state, modifier) }
        is DetailsScreen -> ui<DetailsScreen.State> { state, modifier -> DetailsScreen(state, modifier) }
        else -> null
    }
}
