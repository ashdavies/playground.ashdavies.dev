package io.ashdavies.playground.expansion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.filterIsSuccess
import io.ashdavies.http.parameter
import io.ashdavies.http.requesting
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRequest
import io.ashdavies.playground.serialization.getContent
import io.ashdavies.playground.serialization.getOrThrow
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

private val EXCLUSIONS = listOf("Coffers", "Guilds & Cornucopia")

private inline val JsonElement.title: String
    get() = getContent("title")

private inline val JsonElement.imageInfoUrl: String
    get() = getOrThrow<JsonArray>("imageinfo")
        .firstNotNullOf { it }
        .getContent("url")

/**
 * http://wiki.dominionstrategy.com/api.php?action=query&titles=Expansions&pllimit=max&format=json&prop=links
 * http://wiki.dominionstrategy.com/api.php?action=query&titles=File:Intrigue2.jpg&prop=imageinfo&iiprop=url&format=json
 * http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&prop=sections
 * http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&section=3&prop=links
 * http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&section=9
 */
internal class ExpansionViewModel(private val client: HttpClient) {

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
            .toList()

    suspend fun getViewState(): List<DominionExpansion> {
        val links: Set<String> = client
            .requesting<JsonObject>("api.php") { parameter(DominionRequest.Query.Expansions()) }
            .filterIsSuccess { it.links }
            .first()

        val files: String = links
            .map { it.substring(5, it.length - 4) }
            .filterNot { it in EXCLUSIONS || "File:${it}2.jpg" in links }
            .joinToString("|") { "File:$it.jpg" }

        return client
            .requesting<JsonObject>("api.php") { parameter(DominionRequest.Query.Images(files)) }
            .filterIsSuccess { it.expansions }
            .single()
    }
}

internal fun DominionExpansion(element: JsonElement) = DominionExpansion(
    name = element.title.let { it.substring(5, it.length - 4) },
    image = element.imageInfoUrl,
)

@Composable
internal fun rememberExpansionViewModel(
    client: HttpClient = LocalHttpClient.current,
): ExpansionViewModel = remember(client) {
    ExpansionViewModel(client)
}
