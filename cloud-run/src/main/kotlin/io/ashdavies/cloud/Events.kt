package io.ashdavies.cloud

import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsSerializer
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

internal fun Route.events() {
    get("/events") {
        val provider = DocumentProvider { firestore.collection("events") }
        val reader = CollectionReader<Event>(provider, call.receive())

        call.respond(reader(EventsSerializer))
    }

    post("/events:aggregate") {
        call.respond(HttpStatusCode.NotImplemented)
    }
}
