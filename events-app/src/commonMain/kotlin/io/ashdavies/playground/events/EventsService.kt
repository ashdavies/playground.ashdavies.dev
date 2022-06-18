package io.ashdavies.playground.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.Event
import io.ashdavies.playground.PlaygroundService
import io.ashdavies.playground.getting
import io.ashdavies.playground.invoke
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.content.OutgoingContent.NoContent

internal interface EventsService : PlaygroundService {
    val events: PlaygroundService.Operator<NoContent, List<Event>>
}

@Composable
internal fun rememberEventsService(client: HttpClient = LocalHttpClient.current): EventsService = remember(client) {
    object : EventsService, PlaygroundService by PlaygroundService(client) {
        override val events by getting<NoContent, List<Event>>()
    }
}

internal suspend fun EventsService.events(startAt: String, limit: Int): List<Event> = events {
    parameter("startAt", startAt)
    parameter("limit", limit)
}

