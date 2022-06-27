package io.ashdavies.playground.kingdom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.DominionCard
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRequest
import io.ashdavies.playground.DominionService
import io.ashdavies.playground.DominionViewState
import io.ashdavies.playground.ObsoletePlaygroundApi
import io.ashdavies.playground.produceState
import io.ashdavies.playground.rememberDominionService
import io.ashdavies.playground.serialization.getContent
import io.ashdavies.playground.serialization.getOrThrow
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

private const val KEY_IMAGE_INFO = "imageinfo"
private const val WIKI_QUERY_LIMIT = 50

internal class KingdomViewModel(private val service: DominionService) {

    private val _state = MutableStateFlow<DominionViewState<DominionCard>>(DominionViewState.Ready)
    val state: StateFlow<DominionViewState<DominionCard>> = _state.asStateFlow()

    suspend fun produceEvent(expansion: DominionExpansion) {
        _state.produceState {
            val section = service
                .api(DominionRequest.Parse.Sections(expansion.name))
                .getOrThrow<JsonObject>("parse")
                .getOrThrow<JsonArray>("sections")
                .first { it.isKingdomCards }
                .getContent("index")

            val links = service
                .api(DominionRequest.Parse.Section(expansion.name, section))
                .getOrThrow<JsonObject>("parse")
                .getOrThrow<JsonArray>("links")
                .map { it.getContent("*") }
                .sorted()

            val cards = links.chunked(WIKI_QUERY_LIMIT).flatMap { chunk ->
                chunk
                    .joinToString("|") { "File:$it.jpg" }
                    .let { service.api(DominionRequest.Query.Images(it)) }
                    .getOrThrow<JsonObject>("query", "pages")
                    .filter { it.value.hasImageInfo }
                    .flatMap { it.value.getOrThrow<JsonArray>(KEY_IMAGE_INFO) }
                    .map { it.getContent("url") }
            }

            links
                .associateWith { name -> cards.firstOrNull { name.encoded() in it } }
                .map { DominionCard(expansion, it.key, it.value) }
        }
    }
}

private val JsonElement.hasImageInfo: Boolean
    get() = jsonObject.containsKey(KEY_IMAGE_INFO)

private val JsonElement.isKingdomCards: Boolean
    get() = getContent("line") == "Kingdom cards"

private fun String.encoded(): String =
    replace(" ", "_")
        .replace("'", "%27")

@Composable
@OptIn(ObsoletePlaygroundApi::class)
internal fun rememberKingdomViewModel(client: HttpClient = LocalHttpClient.current): KingdomViewModel =
    remember { KingdomViewModel(DominionService(client)) }
