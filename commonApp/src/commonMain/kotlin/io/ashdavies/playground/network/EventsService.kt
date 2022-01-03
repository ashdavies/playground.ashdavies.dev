package io.ashdavies.playground.network

import io.ashdavies.playground.Event
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val CONFERENCES_API = "https://europe-west1-playground-1a136.cloudfunctions.net/events"

interface EventsService {
    suspend fun get(startAt: String, limit: Int): List<Event>
}

fun EventsService(httpClient: HttpClient) = object : EventsService {
    override suspend fun get(startAt: String, limit: Int): List<Event> {
        return httpClient.get("$CONFERENCES_API?startAt=$startAt&limit=$limit")
    }
}

