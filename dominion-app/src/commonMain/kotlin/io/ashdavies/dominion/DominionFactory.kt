package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.sql.DatabaseFactory
import io.ktor.client.HttpClient

public fun dominionPresenterFactory(context: PlatformContext): Presenter.Factory {
    return Presenter.Factory { screen, navigator, _ ->
        val playgroundDatabase = DatabaseFactory(
            schema = PlaygroundDatabase.Schema,
            context = context,
            factory = PlaygroundDatabase.Companion::invoke,
        )

        when (screen) {
            is DominionScreen.BoxSetList -> presenterOf {
                BoxSetListPresenter(
                    navigator = navigator,
                    boxSetStore = rememberBoxSetStore(playgroundDatabase),
                )
            }

            is DominionScreen.BoxSetDetails -> presenterOf {
                val boxSet = playgroundDatabase.boxSetQueries
                    .selectByTitle(screen.title)
                    .executeAsOne()

                DetailsPresenter(
                    navigator = navigator,
                    cardsStore = rememberCardsStore(playgroundDatabase),
                    boxSet = boxSet,
                )
            }

            else -> null
        }
    }
}

public fun dominionUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is DominionScreen.BoxSetList -> ui<DominionScreen.BoxSetList.State> { state, modifier ->
            BoxSetListScreen(state, modifier)
        }

        is DominionScreen.BoxSetDetails -> ui<DominionScreen.BoxSetDetails.State> { state, modifier ->
            DetailsScreen(state, modifier)
        }

        else -> null
    }
}

@Composable
private fun rememberBoxSetStore(
    playgroundDatabase: PlaygroundDatabase,
    httpClient: HttpClient = LocalHttpClient.current,
): BoxSetStore = remember(playgroundDatabase, httpClient) {
    BoxSetStore(
        boxSetQueries = playgroundDatabase.boxSetQueries,
        httpClient = httpClient,
    )
}

@Composable
private fun rememberCardsStore(
    playgroundDatabase: PlaygroundDatabase,
    httpClient: HttpClient = LocalHttpClient.current,
): CardsStore = remember(playgroundDatabase, httpClient) {
    CardsStore(
        cardQueries = playgroundDatabase.cardQueries,
        httpClient = httpClient,
    )
}