package io.ashdavies.playground

import androidx.compose.material3.ExperimentalMaterial3Api
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
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

public class EventsPresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = when (screen) {
        is HomeScreen -> presenterOf { HomePresenter(navigator) }
        is ActivityScreen -> presenterOf { ActivityPresenter(navigator) }
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

        is ActivityScreen -> ui<ActivityScreen.State> { state, modifier ->
            ActivityScreen(state, modifier)
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
