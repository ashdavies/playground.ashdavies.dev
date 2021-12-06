package io.ashdavies.playground.events

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Query
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.google.await

internal fun interface EventsReader {
    suspend operator fun invoke(): Collection<Event>
}

internal fun EventsReader(reference: CollectionReference, request: EventsRequest) = EventsReader {
    reference
        .startAt(request.startAt)
        .limit(request.limit)
        .readAll()
}

private suspend inline fun <reified T : Any> Query.readAll(): List<T> =
    get()
        .await()
        .toObjects(T::class.java)
