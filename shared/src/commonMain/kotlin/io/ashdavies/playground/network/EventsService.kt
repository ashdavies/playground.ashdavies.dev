package io.ashdavies.playground.network

import io.ashdavies.playground.database.Event
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val CONFERENCES_API =
    "https://europe-west1-playground-1a136.cloudfunctions.net/v1/conferences"

interface EventsService {
    suspend fun get(): List<Event>
}

fun EventsService(httpClient: HttpClient) = object : EventsService {
    override suspend fun get(): List<Event> = httpClient.get(CONFERENCES_API)
}