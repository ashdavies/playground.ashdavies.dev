package io.ashdavies.playground.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.Event
import io.ashdavies.playground.network.LocalHttpClient
import io.ashdavies.playground.network.Service
import io.ashdavies.playground.network.getting
import io.ashdavies.playground.network.invoke
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.content.OutgoingContent.NoContent

public interface EventsService : Service {
    val events: Service.Operator<NoContent, List<Event>>
}

@Composable
public fun rememberEventsService(client: HttpClient = LocalHttpClient.current): EventsService = remember(client) {
    object : EventsService, Service by Service(client) {
        override val events by getting<NoContent, List<Event>>()
    }
}

public suspend fun EventsService.events(startAt: String, limit: Int): List<Event> = events {
    parameter("startAt", startAt)
    parameter("limit", limit)
}

