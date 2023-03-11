package io.ashdavies.playground.events

import io.ashdavies.playground.Event
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class EventsService(private val client: HttpClient) {
    suspend fun fetchItems(startAt: String?, limit: Int): List<Event> = client
        .get("https://playground.ashdavies.dev/events?startAt=$startAt&limit=$limit")
        .body()
}
