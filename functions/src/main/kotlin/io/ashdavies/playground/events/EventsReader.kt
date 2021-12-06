package io.ashdavies.playground.events

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.QuerySnapshot
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.database.EventsSerializer
import io.ashdavies.playground.google.await
import kotlinx.serialization.properties.Properties.Default.decodeFromMap

internal fun interface EventsReader {
    suspend operator fun invoke(): Collection<Event>
}

internal fun EventsReader(reference: CollectionReference, request: EventsQuery) = EventsReader {
    val snapshot: QuerySnapshot = reference
        .orderBy(request.orderBy)
        .startAt(request.startAt)
        .limit(request.limit)
        .get()
        .await()

    snapshot.map {
        decodeFromMap(EventsSerializer, it.data)
    }
}
