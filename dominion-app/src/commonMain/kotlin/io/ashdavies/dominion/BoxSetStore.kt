package io.ashdavies.dominion

import io.ktor.client.HttpClient

private val FILE_ART_REGEX = Regex("File:(.*)Art\\.jpg")
private val FILE_REGEX = Regex("File:(.*)\\.jpg")

private val SET_CATEGORY_REGEX = Regex("Category:(.+)( \\(.+\\))?")
private val SET_TITLE_REGEX = Regex("Category:(.+)")

internal fun interface BoxSetStore : suspend () -> List<BoxSet>

internal fun BoxSetStore(
    boxSetQueries: BoxSetQueries,
    httpClient: HttpClient,
    refresh: Boolean = false,
): BoxSetStore = BoxSetStore {
    val localBoxSet = boxSetQueries.selectAll().executeAsList()
    if (!refresh && localBoxSet.isNotEmpty()) {
        return@BoxSetStore localBoxSet
    }

    val sets = httpClient.categoryMembers("Category:Sets", "subcat")

    val boxImages = httpClient
        .categoryImages("Category:English_box_images", regex = FILE_REGEX)
        .toMutableMap()

    val boxArt = httpClient
        .categoryImages("Category:Box_art", regex = FILE_ART_REGEX)
        .toMutableMap()

    val remoteBoxSetList = sets.mapNotNull { title ->
        val short = title.firstGroupOrNull(SET_CATEGORY_REGEX) ?: title

        BoxSet(
            title = title.firstGroup(SET_TITLE_REGEX),
            image = boxImages.remove(short)?.url ?: run {
                println("Failed to find image for $short")
                return@mapNotNull null
            },
            art = boxArt
                .remove(short)
                ?.url,
        )
    }

    boxSetQueries.transaction {
        remoteBoxSetList.forEach(boxSetQueries::insertOrReplace)
    }

    remoteBoxSetList
}
