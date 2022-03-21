package io.ashdavies.playground.events

import io.ashdavies.playground.Event
import io.ashdavies.playground.network.Service
import io.ashdavies.playground.network.ServiceOperator
import io.ashdavies.playground.network.invoke
import io.ashdavies.playground.network.serviceOperator
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.content.OutgoingContent.NoContent

private const val CONFERENCES_API = "https://europe-west1-playground-1a136.cloudfunctions.net/"

public interface EventsService : Service {
    val events: ServiceOperator<NoContent, List<Event>>
}

public fun EventsService(httpClient: HttpClient) = object : EventsService {
    override val events by serviceOperator<NoContent, List<Event>>(httpClient) { "$CONFERENCES_API/$it" }
}

public suspend fun EventsService.events(startAt: String, limit: Int): List<Event> = events {
    parameter("startAt", startAt)
    parameter("limit", limit)
}

