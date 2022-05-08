package io.ashdavies.playground.expansion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRequest
import io.ashdavies.playground.DominionService
import io.ashdavies.playground.DominionViewState
import io.ashdavies.playground.produceState
import io.ashdavies.playground.rememberDominionService
import io.ashdavies.playground.serialization.getContent
import io.ashdavies.playground.serialization.getOrThrow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

private val EXCLUSIONS = listOf("Coffers", "Guilds & Cornucopia")

internal class ExpansionViewModel(private val service: DominionService) {

    private val _state = MutableStateFlow<DominionViewState<DominionExpansion>>(DominionViewState.Ready)
    val state: StateFlow<DominionViewState<DominionExpansion>> = _state.asStateFlow()

    suspend fun produceEvent() {
        _state.produceState {
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

            service
                .api(DominionRequest.Query.Images(files))
                .getOrThrow<JsonObject>("query", "pages")
                .mapValues { DominionExpansion(it.value) }
                .values
                .toList()
        }
    }
}

internal fun DominionExpansion(element: JsonElement) = DominionExpansion(
    name = element.title.let { it.substring(5, it.length - 4) },
    image = element.url,
)

@Composable
internal fun rememberExpansionViewModel(service: DominionService = rememberDominionService()): ExpansionViewModel =
    remember { ExpansionViewModel(service) }


private inline val JsonElement.title: String
    get() = getContent("title")

private inline val JsonElement.url: String
    get() = getOrThrow<JsonArray>("imageinfo")
        .firstNotNullOf { it }
        .getContent("url")
