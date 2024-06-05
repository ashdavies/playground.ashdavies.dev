package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient

public fun dominionPresenterFactory(): Presenter.Factory {
    return Presenter.Factory { screen, navigator, _ ->
        when (screen) {
            is DominionScreen.BoxSetList -> presenterOf {
                BoxSetListPresenter(
                    navigator = navigator,
                    boxSetStore = rememberBoxSetStore(),
                )
            }

            is DominionScreen.BoxSetDetails -> presenterOf {
                val boxSet = rememberLocalQueries { it.boxSetQueries }
                    .selectByTitle(screen.title)
                    .executeAsOne()

                DetailsPresenter(
                    navigator = navigator,
                    cardsStore = rememberCardsStore(),
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
    boxSetQueries: BoxSetQueries = rememberLocalQueries { it.boxSetQueries },
    httpClient: HttpClient = LocalHttpClient.current,
): BoxSetStore = remember(boxSetQueries, httpClient) {
    BoxSetStore(
        boxSetQueries = boxSetQueries,
        httpClient = httpClient,
    )
}

@Composable
private fun rememberCardsStore(
    cardQueries: CardQueries = rememberLocalQueries { it.cardQueries },
    httpClient: HttpClient = LocalHttpClient.current,
): CardsStore = remember(cardQueries, httpClient) {
    CardsStore(
        cardQueries = cardQueries,
        httpClient = httpClient,
        refresh = true,
    )
}
