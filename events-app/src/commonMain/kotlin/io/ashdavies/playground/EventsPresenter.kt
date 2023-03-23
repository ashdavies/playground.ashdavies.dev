package io.ashdavies.playground

import androidx.compose.material3.ExperimentalMaterial3Api
import com.slack.circuit.CircuitContext
import com.slack.circuit.Navigator
import com.slack.circuit.Presenter
import com.slack.circuit.Screen
import com.slack.circuit.Ui
import com.slack.circuit.presenterOf
import com.slack.circuit.ui
import io.ashdavies.playground.details.DetailsPresenter
import io.ashdavies.playground.details.DetailsScreen
import io.ashdavies.playground.home.HomePresenter
import io.ashdavies.playground.home.HomeScreen
import io.ashdavies.playground.profile.ProfilePresenter
import io.ashdavies.playground.profile.ProfileScreen

public class EventsPresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = when (screen) {
        is HomeScreen -> presenterOf { HomePresenter(navigator) }
        is ProfileScreen -> presenterOf { ProfilePresenter(navigator) }
        is DetailsScreen -> presenterOf { DetailsPresenter(navigator, screen.eventId) }
        else -> null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
public class EventsUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is HomeScreen -> ui<HomeScreen.State> { state, modifier ->
            HomeScreen(state, modifier)
        }

        is ProfileScreen -> ui<ProfileScreen.State> { state, modifier ->
            ProfileScreen(state, modifier)
        }

        is DetailsScreen -> ui<DetailsScreen.State> { state, modifier ->
            DetailsScreen(state, modifier)
        }

        else -> null
    }
}
