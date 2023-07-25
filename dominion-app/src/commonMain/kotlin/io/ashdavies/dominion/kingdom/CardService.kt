package io.ashdavies.dominion.kingdom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.dominion.DOMINION_STRATEGY_URL
import io.ashdavies.dominion.DominionCard
import io.ashdavies.dominion.DominionExpansion
import io.ashdavies.dominion.DominionRequest
import io.ashdavies.dominion.serialization.getContent
import io.ashdavies.dominion.serialization.getOrThrow
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.parameter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

private const val KEY_IMAGE_INFO = "imageinfo"
private const val WIKI_QUERY_LIMIT = 50

private val JsonElement.hasImageInfo: Boolean
    get() = jsonObject.containsKey(KEY_IMAGE_INFO)

private val JsonElement.isKingdomCards: Boolean
    get() = getContent("line") == "Kingdom cards"

private fun String.encoded(): String = replace(" ", "_")
    .replace("'", "%27")

internal class CardService(private val client: HttpClient) {

    private val JsonObject.section: String
        get() = getOrThrow<JsonObject>("parse")
            .getOrThrow<JsonArray>("sections")
            .first { it.isKingdomCards }
            .getContent("index")

    private val JsonObject.links: List<String>
        get() = getOrThrow<JsonObject>("parse")
            .getOrThrow<JsonArray>("links")
            .map { it.getContent("*") }
            .sorted()

    private val JsonObject.url: List<String>
        get() = getOrThrow<JsonObject>("query", "pages")
            .filter { it.value.hasImageInfo }
            .flatMap { it.value.getOrThrow<JsonArray>(KEY_IMAGE_INFO) }
            .map { it.getContent("url") }

    suspend fun getCardList(expansion: DominionExpansion): List<DominionCard> {
        val section = client
            .get(DOMINION_STRATEGY_URL) {
                parameter(DominionRequest.Parse.Sections(expansion.name))
            }
            .body<JsonObject>()
            .section

        val links: List<String> = client
            .get(DOMINION_STRATEGY_URL) {
                parameter(DominionRequest.Parse.Section(expansion.name, section))
            }
            .body<JsonObject>()
            .links

        suspend fun images(titles: String): List<String> = client
            .get(DOMINION_STRATEGY_URL) {
                parameter(DominionRequest.Query.Images(titles))
            }
            .body<JsonObject>()
            .url

        val images: List<String> = links
            .chunked(WIKI_QUERY_LIMIT)
            .flatMap { chunk -> images(chunk.joinToString("|") { "File:$it.jpg" }) }

        return links
            .associateWith { name -> images.firstOrNull { name.encoded() in it } }
            .map { DominionCard(expansion, it.key, it.value) }
    }
}
