package io.ashdavies.playground.events

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.QuerySnapshot
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.google.await

internal fun interface EventsReader {
    suspend operator fun invoke(): Collection<Event>
}

internal fun EventsReader(reference: CollectionReference, request: EventsQuery) = EventsReader {
    reference
        .orderBy(request.orderBy)
        .startAt(request.startAt)
        .limit(request.limit)
        .get()
        .await()
        .toObjects()
}

private inline fun <reified T : Any> QuerySnapshot.toObjects(): List<T> = toObjects(T::class.java)
