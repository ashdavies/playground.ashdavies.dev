package io.ashdavies.dominion.kingdom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.filterIsSuccess
import io.ashdavies.http.parameter
import io.ashdavies.http.requesting
import io.ashdavies.dominion.DominionCard
import io.ashdavies.dominion.DominionExpansion
import io.ashdavies.dominion.DominionRequest
import io.ashdavies.dominion.serialization.getContent
import io.ashdavies.dominion.serialization.getOrThrow
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
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

internal class KingdomViewModel(private val scope: CoroutineScope, private val client: HttpClient) {

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

    suspend fun getViewState(expansion: DominionExpansion): List<DominionCard> {
        val section = client
            .requesting<JsonObject>("api.php") { parameter(DominionRequest.Parse.Sections(expansion.name)) }
            .filterIsSuccess { it.section }
            .first()

        val links: List<String> = client
            .requesting<JsonObject>("api.php") { parameter(DominionRequest.Parse.Section(expansion.name, section)) }
            .filterIsSuccess { it.links }
            .first()

        suspend fun images(titles: String): List<String> = client
            .requesting<JsonObject>("api.php") { parameter(DominionRequest.Query.Images(titles)) }
            .filterIsSuccess { it.url }
            .first()

        val images: List<String> = links
            .chunked(WIKI_QUERY_LIMIT)
            .flatMap { chunk -> images(chunk.joinToString("|") { "File:$it.jpg" }) }

        return links
            .associateWith { name -> images.firstOrNull { name.encoded() in it } }
            .map { DominionCard(expansion, it.key, it.value) }
    }

    fun getViewStateFlow(expansion: DominionExpansion): StateFlow<KingdomViewState> = TODO()
}

@Serializable
internal sealed class KingdomViewState : Parcelable {

    @Parcelize
    @Serializable
    object Ready : KingdomViewState()

    @Parcelize
    @Serializable
    object Loading : KingdomViewState()

    @Parcelize
    @Serializable
    data class Success(val value: List<DominionCard>) : KingdomViewState()
}

@Composable
internal fun rememberKingdomViewModel(
    scope: CoroutineScope = rememberCoroutineScope(),
    client: HttpClient = LocalHttpClient.current,
): KingdomViewModel = remember(client) {
    KingdomViewModel(scope, client)
}
