package io.ashdavies.dominion

import io.ktor.client.HttpClient

private val FILE_ART_REGEX = Regex("File:(.*)Art\\.jpg")
private val FILE_REGEX = Regex("File:(.*)\\.jpg")
private val TITLE_REGEX = Regex("(.+) \\(.+\\)")

// TODO Rename to Set

internal fun interface ExpansionsStore : suspend () -> List<Expansion>

internal fun ExpansionsStore(
    expansionQueries: ExpansionQueries,
    httpClient: HttpClient,
    refresh: Boolean = false,
): ExpansionsStore = ExpansionsStore {
    val expansions = expansionQueries.selectAll().executeAsList()
    if (!refresh && expansions.isNotEmpty()) {
        return@ExpansionsStore expansions
    }

    val sets = httpClient.categoryMembers("Category:Sets", "page")

    val boxImages = httpClient
        .categoryImages("Category:English_box_images", FILE_REGEX)
        .toMutableMap()

    val boxArt = httpClient
        .categoryImages("Category:Box_art", FILE_ART_REGEX)
        .toMutableMap()

    sets.mapNotNull { title ->
        val short = title.firstGroupOrNull(TITLE_REGEX) ?: title

        Expansion(
            title = title,
            image = boxImages.remove(short) ?: run {
                println("Failed to find image for $short")
                return@mapNotNull null
            },
            art = boxArt.remove(short),
        ).also(expansionQueries::insertOrReplace)
    }
}
