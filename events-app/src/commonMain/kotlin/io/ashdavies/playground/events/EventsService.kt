package io.ashdavies.playground.events

import io.ashdavies.http.filterIsSuccess
import io.ashdavies.http.parameter
import io.ashdavies.http.requesting
import io.ashdavies.playground.Event
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.single

internal class EventsService(private val client: HttpClient) {
    suspend fun fetchItems(startAt: String?, limit: Int): List<Event> = client
        .requesting<List<Event>>("events") { parameter(EventsRequest(startAt, limit)) }
        .filterIsSuccess()
        .single()
}
