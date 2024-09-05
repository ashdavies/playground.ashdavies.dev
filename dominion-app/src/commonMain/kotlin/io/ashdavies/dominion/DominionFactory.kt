package io.ashdavies.dominion

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ashdavies.http.throwClientRequestExceptionAs
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator

private fun HttpClient.throwDbConnectionError(): HttpClient = config {
    install(HttpCallValidator) {
        throwClientRequestExceptionAs<DbConnectionError>()
    }

    expectSuccess = true
}

public fun Circuit.Builder.addDominionBoxSetListPresenter(
    playgroundDatabase: PlaygroundDatabase,
    httpClient: HttpClient,
): Circuit.Builder {
    val boxSetQueries = playgroundDatabase.boxSetQueries
    val dbHttpClient = httpClient.throwDbConnectionError()
    val boxSetStore = BoxSetStore(boxSetQueries, dbHttpClient)

    return addPresenter<DominionScreen.BoxSetList, DominionScreen.BoxSetList.State> { _, navigator, _ ->
        presenterOf { BoxSetListPresenter(navigator, boxSetStore) }
    }
}

public fun Circuit.Builder.addDominionBoxSetDetailsPresenter(
    playgroundDatabase: PlaygroundDatabase,
    httpClient: HttpClient,
): Circuit.Builder {
    val cardsStore = CardsStore(
        cardQueries = playgroundDatabase.cardQueries,
        httpClient = httpClient.throwDbConnectionError(),
        refresh = true,
    )

    return addPresenter<DominionScreen.BoxSetDetails, DominionScreen.BoxSetDetails.State> { screen, navigator, _ ->
        val boxSet = playgroundDatabase.boxSetQueries
            .selectByTitle(screen.title)
            .executeAsOne()

        presenterOf {
            DetailsPresenter(navigator, cardsStore, boxSet)
        }
    }
}

public fun Circuit.Builder.addDominionBoxSetListUi(): Circuit.Builder {
    return addUi<DominionScreen.BoxSetList, DominionScreen.BoxSetList.State> { state, modifier ->
        BoxSetListScreen(state, modifier)
    }
}

public fun Circuit.Builder.addDominionBoxSetDetailsUi(): Circuit.Builder {
    return addUi<DominionScreen.BoxSetDetails, DominionScreen.BoxSetDetails.State> { state, modifier ->
        DetailsScreen(state, modifier)
    }
}
