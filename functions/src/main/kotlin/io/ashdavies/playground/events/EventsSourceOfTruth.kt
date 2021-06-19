package io.ashdavies.playground.events

import com.google.cloud.firestore.CollectionReference
import io.ashdavies.playground.google.readAll

internal class EventsSourceOfTruth(
    private val collection: CollectionReference,
    private val startAt: String,
    private val limit: Int,
) : CollectionSourceOfTruth<Event>(collection, Event::id) {

    override suspend fun nonFlowReader(key: Unit): List<Event> {
        return collection
            .startAt(startAt)
            .limit(limit)
            .readAll()
    }
}