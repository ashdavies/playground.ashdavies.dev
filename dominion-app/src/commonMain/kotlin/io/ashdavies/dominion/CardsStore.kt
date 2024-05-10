package io.ashdavies.dominion

import io.ktor.client.HttpClient

private const val DEFAULT_LIMIT = 500

private val CARD_IMAGE_REGEX = Regex("File:(.*)\\.jpg")

internal fun interface CardsStore : suspend (String) -> List<Card>

internal fun CardsStore(
    cardQueries: CardQueries,
    httpClient: HttpClient,
    refresh: Boolean = false,
): CardsStore = CardsStore { title ->
    val allLocalCards = cardQueries.selectAll().executeAsList()
    val localBoxSetCards = allLocalCards.filter {
        it.boxSet == title
    }

    if (!refresh && localBoxSetCards.isNotEmpty()) {
        return@CardsStore localBoxSetCards
    }

    val allCards = allLocalCards.takeIf { it.isNotEmpty() } ?: buildList {
        var offset = 0

        do {
            val images = httpClient.categoryImages(
                gcmTitle = "Category:English_card_images",
                gcmLimit = DEFAULT_LIMIT,
                gcmOffset = offset,
                regex = CARD_IMAGE_REGEX,
            )

            if (offset > DEFAULT_LIMIT * 2) {
                println("Not correctly offsetting request...")
                break
            }

            val cards = images.map {
                Card(
                    title = it.key,
                    image = requireNotNull(it.value) {
                        "Image not found for card ${it.key}"
                    },
                    boxSet = null,
                )
            }

            offset = images.size
            addAll(cards)
        } while (images.size >= 500)
    }

    val allCardsByTitle = allCards.associateBy { it.title }

    httpClient
        .categoryMembers("Category:$title", "subcat")
        .flatMap { httpClient.categoryMembers(it, "page") }
        .map { allCardsByTitle.getValue(it) }
}
