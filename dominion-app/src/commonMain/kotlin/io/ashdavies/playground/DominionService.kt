package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

// http://wiki.dominionstrategy.com/api.php?action=query&titles=Expansions&pllimit=max&format=json&prop=links
// http://wiki.dominionstrategy.com/api.php?action=query&titles=File:Intrigue2.jpg&prop=imageinfo&iiprop=url&format=json

// http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&prop=sections
// http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&section=9

private const val DOMINION_STRATEGY = "http://wiki.dominionstrategy.com"

internal interface DominionService : PlaygroundService {
    val api: PlaygroundService.Operator<DominionRequest, JsonObject>
}

@Composable
internal fun rememberDominionService(client: HttpClient = LocalHttpClient.current): DominionService = remember(client) {
    object : DominionService, PlaygroundService by PlaygroundService(client) {
        override val api by getting<DominionRequest, JsonObject> { "$DOMINION_STRATEGY/$it.php" }
    }
}

@Serializable
internal sealed class DominionRequest(val format: String = "json") {

    @Serializable
    sealed class Query(val action: String = "query") : DominionRequest() {

        @Serializable
        data class Expansions(
            val titles: String = "Expansions",
            val pllimit: String = "max",
            val prop: String = "links",
        ) : Query()

        @Serializable
        data class Images(
            val titles: String,
            val prop: String = "imageinfo",
            val iiprop: String = "url"
        ) : Query()
    }

    // http://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&section=3&prop=links
    @Serializable
    sealed class Parse(val action: String = "parse") : DominionRequest() {

        @Serializable
        data class Sections(
            val page: String,
            val prop: String = "sections"
        ) : Parse()

        @Serializable
        data class Section(
            val page: String,
            val section: String,
            val prop: String = "links"
        ) : Parse()
    }
}
