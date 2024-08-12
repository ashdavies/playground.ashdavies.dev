package io.ashdavies.cloud

import io.ashdavies.aggregator.AsgConference
import io.ashdavies.aggregator.AsgService
import io.ashdavies.cloud.operations.AggregateEventsOperation
import io.ashdavies.cloud.operations.UpcomingEventsOperation
import io.ashdavies.http.common.models.Event
import io.ktor.client.HttpClient
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

internal fun Route.events(httpClient: HttpClient) {
    val documentProvider = DocumentProvider { firestore.collection("events") }
    val upcomingEvents = UpcomingEventsOperation(documentProvider)

    val aggregateEvents = AggregateEventsOperation(
        documentProvider = documentProvider,
        collectionWriter = CollectionWriter(documentProvider, Event::id),
        asgService = AsgService(httpClient),
        identifier = HashIdentifier(AsgConference.serializer()),
    )

    get("/events/upcoming") { upcomingEvents(call) }

    post("/events:aggregate") { aggregateEvents(call) }
}
