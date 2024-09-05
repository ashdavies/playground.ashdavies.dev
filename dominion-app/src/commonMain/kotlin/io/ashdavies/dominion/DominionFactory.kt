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

public fun Circuit.Builder.addDominionPresenter(
    playgroundDatabase: PlaygroundDatabase,
    httpClient: HttpClient,
): Circuit.Builder {
    val boxSetQueries = playgroundDatabase.boxSetQueries
    val dbHttpClient = httpClient.throwDbConnectionError()
    val boxSetStore = BoxSetStore(boxSetQueries, dbHttpClient)

    val cardsStore = CardsStore(
        cardQueries = playgroundDatabase.cardQueries,
        httpClient = dbHttpClient,
        refresh = true,
    )

    return addPresenter<DominionScreen.AdaptiveList, DominionScreen.AdaptiveList.State> { _, _, _ ->
        presenterOf { DominionPresenter(boxSetStore, cardsStore) }
    }
}

public fun Circuit.Builder.addDominionUi(): Circuit.Builder {
    return addUi<DominionScreen.AdaptiveList, DominionScreen.AdaptiveList.State> { state, modifier ->
        DominionScreen(state, modifier)
    }
}
