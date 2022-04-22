package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ktor.client.HttpClient
import io.ktor.http.content.OutgoingContent.NoContent

// https://wiki.dominionstrategy.com/api.php?action=query&titles=Expansions&pllimit=max&format=json[&prop=links]
// https://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&prop=sections
// https://wiki.dominionstrategy.com/api.php?action=parse&format=json&page=Dominion_(Base_Set)&section=9

internal interface DominionService : PlaygroundService {
    val api: PlaygroundService.Operator<NoContent, String>
}

@Composable
internal fun rememberDominionService(client: HttpClient = LocalHttpClient.current): DominionService = remember(client) {
    object : DominionService, PlaygroundService by PlaygroundService(client) {
        override val api by getting<NoContent, String> { "$it.php?action=query&format=json&pllimit=max&titles=Expansions" }
    }
}
