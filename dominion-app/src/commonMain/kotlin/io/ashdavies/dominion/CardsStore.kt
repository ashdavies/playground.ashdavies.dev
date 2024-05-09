package io.ashdavies.dominion

import io.ktor.client.HttpClient

internal fun interface CardsStore : suspend (String) -> List<Card>

internal fun CardsStore(
    cardQueries: CardQueries,
    httpClient: HttpClient,
    refresh: Boolean = false,
): CardsStore = CardsStore { expansion ->
    val cards = cardQueries.selectByExpansion(expansion).executeAsList()
    if (!refresh && cards.isNotEmpty()) {
        return@CardsStore cards
    }

    TODO()
}
