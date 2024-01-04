package io.ashdavies.dominion.home

import io.ashdavies.dominion.DOMINION_STRATEGY_URL
import io.ashdavies.dominion.DominionExpansion
import io.ashdavies.dominion.DominionRequest
import io.ashdavies.dominion.serialization.getContent
import io.ashdavies.dominion.serialization.getOrThrow
import io.ashdavies.http.parameter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

private val EXCLUSIONS = listOf("Coffers", "Guilds & Cornucopia")

private inline val JsonElement.title: String
    get() = getContent("title")

private inline val JsonElement.imageInfoUrl: String?
    get() = (jsonObject["imageinfo"] as? JsonArray)
        ?.firstNotNullOf { it }
        ?.getContent("url")

/**
 * http://wiki.dominionstrategy.com/api.php?action=query&titles=Expansions&pllimit=max&format=json&prop=links
 * http://wiki.dominionstrategy.com/api.php?action=query&titles=File:Intrigue2.jpg&prop=imageinfo&iiprop=url&format=json
 * http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&prop=sections
 * http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&section=3&prop=links
 * http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&section=9
 */
internal class ExpansionService(private val client: HttpClient) {

    private val JsonObject.links: Set<String>
        get() = getOrThrow<JsonObject>("query", "pages", "157")
            .getOrThrow<JsonArray>("links")
            .map { it.getContent("title") }
            .filter { it.startsWith("File:") }
            .toSet()

    private val JsonObject.expansions: List<DominionExpansion>
        get() = getOrThrow<JsonObject>("query", "pages")
            .mapValues { DominionExpansion(it.value) }
            .values
            .filterNotNull()
            .toList()

    suspend fun getExpansionList(): List<DominionExpansion> {
        val links: Set<String> = client
            .get(DOMINION_STRATEGY_URL) { parameter(DominionRequest.Query.Expansions()) }
            .body<JsonObject>()
            .also { println(it) }
            .links

        val files: String = links
            .map { it.substring(5, it.length - 4) }
            .filterNot { it in EXCLUSIONS || "File:${it}2.jpg" in links }
            .joinToString("|") { "File:$it.jpg" }

        return client
            .get(DOMINION_STRATEGY_URL) { parameter(DominionRequest.Query.Images(files)) }
            .body<JsonObject>()
            .expansions
    }
}

internal fun DominionExpansion(element: JsonElement): DominionExpansion? {
    val image = element.imageInfoUrl ?: return null
    return DominionExpansion(
        name = element.title.let { it.substring(5, it.length - 4) },
        image = image,
    )
}
