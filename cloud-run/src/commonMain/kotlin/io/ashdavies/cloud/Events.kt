package io.ashdavies.cloud

import io.ashdavies.aggregator.AsgConference
import io.ashdavies.aggregator.AsgService
import io.ashdavies.http.common.models.Event
import io.ashdavies.http.common.models.EventCfp
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

private const val DEFAULT_ORDER_BY = "dateStart"
private const val DEFAULT_LIMIT = 50

internal fun Route.events(client: HttpClient) {
    val provider = DocumentProvider { firestore.collection("events") }
    val identifier = HashIdentifier(AsgConference.serializer())
    val writer = CollectionWriter(provider, Event::id)
    val service = AsgService(client)

    get("/events/upcoming") {
        val startAt = call.request.queryParameters["startAt"] ?: todayAsString()
        val limit = call.request.queryParameters["limit"]?.toInt() ?: DEFAULT_LIMIT

        val query = CollectionQuery(DEFAULT_ORDER_BY, startAt, limit)
        val reader = CollectionReader<Event>(provider, query)

        call.respond(reader(Event.serializer()))
    }

    post("/events:aggregate") {
        val reader = CollectionReader<Event>(provider, CollectionQuery(limit = 0))

        writer(reader(), service { it.toEvent(identifier(it)) })
        call.respond(HttpStatusCode.OK)
    }
}

private fun AsgConference.toEvent(id: String) = Event(
    id = id, name = name, website = website, location = location, imageUrl = imageUrl,
    dateStart = dateStart, dateEnd = dateEnd, status = status, online = online,
    cfp = cfp?.toEventCfp(),
)

private fun AsgConference.Cfp.toEventCfp() = EventCfp(start = start, end = end, site = site)

private fun todayAsString(): String = Clock.System
    .todayIn(TimeZone.currentSystemDefault())
    .toString()
