package io.ashdavies.playground.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.Event
import io.ashdavies.playground.PlaygroundService
import io.ashdavies.playground.getting
import io.ashdavies.playground.invoke
import io.ashdavies.playground.LocalHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.content.OutgoingContent.NoContent

private const val PLAYGROUND_FUNCTIONS_HOST = "https://europe-west1-playground-1a136.cloudfunctions.net/"

internal interface EventsService : PlaygroundService {
    val events: PlaygroundService.Operator<NoContent, List<Event>>
}

@Composable
internal fun rememberEventsService(client: HttpClient = LocalHttpClient.current): EventsService = remember(client) {
    object : EventsService, PlaygroundService by PlaygroundService(client) {
        override val events by getting<NoContent, List<Event>> { "$PLAYGROUND_FUNCTIONS_HOST/$it" }
    }
}

internal suspend fun EventsService.events(startAt: String, limit: Int): List<Event> = events {
    parameter("startAt", startAt)
    parameter("limit", limit)
}

