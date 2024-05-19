package io.ashdavies.dominion

import io.ktor.client.HttpClient

private const val DEFAULT_LIMIT = 500

private val CARD_IMAGE_REGEX = Regex("File:(.*)\\.(jpg|png)", RegexOption.IGNORE_CASE)

internal fun interface CardsStore : suspend (String) -> List<Card>

internal fun CardsStore(
    cardQueries: CardQueries,
    httpClient: HttpClient,
    refresh: Boolean = false,
): CardsStore = CardsStore local@{ boxSetTitle ->
    val allLocalCards = cardQueries.selectAll().executeAsList()
    val localBoxSetCards = allLocalCards.filter {
        it.boxSet == boxSetTitle
    }

    if (!refresh && localBoxSetCards.isNotEmpty()) {
        return@local localBoxSetCards
    }

    val boxSetCardTitles = httpClient
        .categoryMembers("Category:$boxSetTitle", "subcat")
        .flatMap { httpClient.categoryMembers(it, "page") }
        .toHashSet()

    val boxSetCards = mutableListOf<Card>()

    val allCards = allLocalCards
        .takeIf { !refresh && it.isNotEmpty() }
        ?: httpClient.allCategoryImages().map { (cardTitle, imageInfo) ->
            val isInBoxSet = cardTitle in boxSetCardTitles

            Card(
                title = cardTitle,
                format = when {
                    imageInfo.size.width > imageInfo.size.height -> CardFormat.HORIZONTAL
                    else -> CardFormat.VERTICAL
                },
                image = imageInfo.url,
                boxSet = if (isInBoxSet) boxSetTitle else null,
            ).also {
                if (isInBoxSet) {
                    boxSetCards.add(it)
                }
            }
        }

    cardQueries.transaction {
        allCards.forEach(cardQueries::insertOrReplace)
    }

    boxSetCards
}

private suspend fun HttpClient.allCategoryImages(): Map<String, ImageInfo> = buildMap {
    var offset = 0

    do {
        val result = categoryImages(
            gcmTitle = "Category:English_card_images",
            gcmLimit = DEFAULT_LIMIT,
            gcmOffset = offset,
            regex = CARD_IMAGE_REGEX,
        )

        if (offset > DEFAULT_LIMIT * 2) {
            println("Not correctly offsetting request...")
            break
        }

        offset += result.size
        putAll(result)
    } while (result.size == DEFAULT_LIMIT)
}
