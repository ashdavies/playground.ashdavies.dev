package io.ashdavies.playground.network

import io.ashdavies.playground.database.Event
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.todayAt

private const val CONFERENCES_API = "https://europe-west1-playground-1a136.cloudfunctions.net/events"
private const val DEFAULT_LIMIT = 20

interface EventsService {
    suspend fun get(startAt: String = today(), limit: Int = DEFAULT_LIMIT): List<Event>
}

fun EventsService(httpClient: HttpClient) = object : EventsService {
    override suspend fun get(startAt: String, limit: Int): List<Event> {
        return httpClient.get("$CONFERENCES_API?startAt=$startAt&limit=$limit")
    }
}

private fun today(): String = Clock.System
    .todayAt(currentSystemDefault())
    .toString()
