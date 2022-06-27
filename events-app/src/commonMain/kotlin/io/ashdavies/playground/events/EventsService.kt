package io.ashdavies.playground.events

import io.ashdavies.playground.Event
import io.ashdavies.playground.ObsoletePlaygroundApi
import io.ashdavies.playground.PlaygroundService
import io.ashdavies.playground.parameters
import io.ashdavies.playground.requesting
import io.ktor.client.HttpClient

@ObsoletePlaygroundApi
internal class EventsService(client: HttpClient) : PlaygroundService(client) {
    val events: Operator<EventsRequest, List<Event>> by requesting { parameters(it) }
}

internal data class EventsRequest(val startAt: String, val limit: Int)
