package io.ashdavies.playground

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public class RatingsPresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = when (screen) {
        is RatingsScreen -> presenterOf { RatingsPresenter(navigator) }
        else -> null
    }
}

public class RatingsUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is RatingsScreen -> ui<RatingsScreen.State> { state, modifier ->
            RatingsScreen(state, modifier)
        }

        else -> null
    }
}
