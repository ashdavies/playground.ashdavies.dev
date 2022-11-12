package io.ashdavies.playground.events

import com.google.cloud.firestore.QuerySnapshot
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsSerializer
import io.ashdavies.playground.google.DocumentProvider
import io.ashdavies.playground.google.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties.Default.decodeFromMap

public fun interface EventsReader {
    public suspend operator fun invoke(): List<Event>
}

@OptIn(ExperimentalSerializationApi::class)
@Suppress("NO_EXPLICIT_RETURN_TYPE_IN_API_MODE_WARNING")
public fun EventsReader(provider: DocumentProvider, request: EventsQuery) = EventsReader {
    val snapshot: QuerySnapshot = provider.invoke {
        orderBy = request.orderBy

        if (request.limit > 0) {
            limit = request.limit
        }

        request.startAt?.let {
            startAt = it
        }
    }.await()

    snapshot.map {
        decodeFromMap(EventsSerializer, it.data)
    }
}
