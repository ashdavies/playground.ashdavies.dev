package io.ashdavies.dominion

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.sql.DatabaseFactory

public fun dominionPresenterFactory(context: PlatformContext): Presenter.Factory {
    return Presenter.Factory { screen, navigator, _ ->
        val playgroundDatabase = DatabaseFactory(
            schema = PlaygroundDatabase.Schema,
            context = context,
            factory = PlaygroundDatabase.Companion::invoke,
        )

        when (screen) {
            is DominionScreen.Expansions -> presenterOf {
                ExpansionsPresenter(
                    navigator = navigator,
                    expansionsQueries = playgroundDatabase.expansionQueries,
                    httpClient = LocalHttpClient.current
                )
            }

            is DominionScreen.Kingdom -> presenterOf {
                KingdomPresenter(
                    navigator = navigator,
                    cardQueries = playgroundDatabase.cardQueries,
                    httpClient = LocalHttpClient.current,
                    expansion = screen.expansion,
                )
            }

            else -> null
        }
    }
}

public fun dominionUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is DominionScreen.Expansions -> ui<DominionScreen.Expansions.State> { state, modifier ->
            ExpansionsScreen(state, modifier)
        }

        is DominionScreen.Kingdom -> ui<DominionScreen.Kingdom.State> { state, modifier ->
            KingdomScreen(state, modifier)
        }

        else -> null
    }
}
