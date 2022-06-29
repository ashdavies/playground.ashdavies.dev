package io.ashdavies.playground.expansion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRequest
import io.ashdavies.playground.DominionService
import io.ashdavies.playground.ObsoletePlaygroundApi
import io.ashdavies.playground.serialization.getContent
import io.ashdavies.playground.serialization.getOrThrow
import io.ktor.client.HttpClient
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

private val EXCLUSIONS = listOf("Coffers", "Guilds & Cornucopia")

@OptIn(ObsoletePlaygroundApi::class)
internal class ExpansionViewModel(private val service: DominionService) {

    suspend fun getViewState(): List<DominionExpansion> {
        val links: Set<String> = service
            .api(DominionRequest.Query.Expansions())
            .getOrThrow<JsonObject>("query", "pages", "157")
            .getOrThrow<JsonArray>("links")
            .map { it.getContent("title") }
            .filter { it.startsWith("File:") }
            .toSet()

        val files: String = links
            .map { it.substring(5, it.length - 4) }
            .filterNot { it in EXCLUSIONS || "File:${it}2.jpg" in links }
            .joinToString("|") { "File:$it.jpg" }

        return service
            .api(DominionRequest.Query.Images(files))
            .getOrThrow<JsonObject>("query", "pages")
            .mapValues { DominionExpansion(it.value) }
            .values
            .toList()
    }
}

internal fun DominionExpansion(element: JsonElement) = DominionExpansion(
    name = element.title.let { it.substring(5, it.length - 4) },
    image = element.url,
)

@Composable
@OptIn(ObsoletePlaygroundApi::class)
internal fun rememberExpansionViewModel(client: HttpClient = LocalHttpClient.current): ExpansionViewModel {
    return remember { ExpansionViewModel(DominionService(client)) }
}


private inline val JsonElement.title: String
    get() = getContent("title")

private inline val JsonElement.url: String
    get() = getOrThrow<JsonArray>("imageinfo")
        .firstNotNullOf { it }
        .getContent("url")
