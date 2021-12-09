package io.ashdavies.playground.events

import com.google.cloud.firestore.QuerySnapshot
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.database.EventsSerializer
import io.ashdavies.playground.google.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties.Default.decodeFromMap

fun interface EventsReader {
    suspend operator fun invoke(): Collection<Event>
}

@OptIn(ExperimentalSerializationApi::class)
fun EventsReader(provider: DocumentProvider, request: EventsQuery) = EventsReader {
    val snapshot: QuerySnapshot = provider {
        orderBy(request.orderBy)
        startAt(request.startAt)
        limit(request.limit)
    }.await()

    snapshot.map {
        decodeFromMap(EventsSerializer, it.data)
    }
}
