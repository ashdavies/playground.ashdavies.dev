package io.ashdavies.playground.expansion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.DominionExpansion
import io.ashdavies.playground.DominionRequest
import io.ashdavies.playground.DominionService
import io.ashdavies.playground.rememberDominionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val EXCLUSIONS = listOf("Coffers", "Guilds & Cornucopia")

internal class ExpansionViewModel(private val service: DominionService) {

    private val _state = MutableStateFlow<ExpansionViewState>(ExpansionViewState.Ready)
    val state: StateFlow<ExpansionViewState> = _state.asStateFlow()

    suspend fun produceEvent() {
        _state.emit(ExpansionViewState.Loading)

        val links: Set<String> = service
            .api(DominionRequest.Query.Expansions())
            .getOrThrow<JsonObject>("query", "pages", "157")
            .getOrThrow<JsonArray>("links")
            .map { it.jsonObject.title }
            .toSet()

        val files: String = links
            .filter { it.startsWith("File:") }
            .map { it.substring(5, it.length - 4) }
            .filterNot { it in EXCLUSIONS || "File:${it}2.jpg" in links }
            .joinToString("|") { "File:$it.jpg" }

        val expansions: List<DominionExpansion> = service
            .api(DominionRequest.Query.Images(files))
            .getOrThrow<JsonObject>("query", "pages")
            .mapValues { DominionExpansion(it.value) }
            .values
            .toList()

        _state.emit(ExpansionViewState.Success(expansions))
    }
}

internal fun DominionExpansion(element: JsonElement) = DominionExpansion(
    name = element.title,
    image = element.url,
)

internal sealed interface ExpansionViewState {
    object Ready : ExpansionViewState
    object Loading : ExpansionViewState

    data class Success(val expansions: List<DominionExpansion>) : ExpansionViewState
}

@Composable
internal fun rememberExpansionViewModel(service: DominionService = rememberDominionService()): ExpansionViewModel =
    remember { ExpansionViewModel(service) }


private inline val JsonElement.title: String
    get() = jsonObject.getContent("title")

private inline val JsonElement.url: String
    get() = jsonObject
        .getOrThrow<JsonArray>("imageinfo")
        .firstNotNullOf { it.jsonObject }
        .getContent("url")

private fun JsonObject.getContent(key: String): String =
    getValue(key).jsonPrimitive.content

private inline fun <reified T : JsonElement> JsonObject.getOrThrow(vararg keys: String): T =
    keys.fold(this) { acc, key -> acc.getOrThrow(key) } as? T ?: error(simpleName<T>())

private inline fun <reified T : JsonElement> JsonObject.getOrThrow(key: String): T =
    getOrElse(key) { throw IllegalStateException() } as? T ?: error(simpleName<T>())

private inline fun <reified T> simpleName(): String =
    T::class.simpleName ?: error(T::class)
