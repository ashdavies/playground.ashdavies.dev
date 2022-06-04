package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.EventsSerializer
import io.ashdavies.playground.firebase.DocumentProvider
import io.ashdavies.playground.firebase.FirebaseFunction
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private const val COLLECTION_PATH = "events"

internal class EventsFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val eventsReader = EventsReader(
            provider = DocumentProvider(COLLECTION_PATH),
            request = EventsQuery(request),
        )

        return Json.encodeToString(
            serializer = ListSerializer(EventsSerializer),
            value = eventsReader()
        )
    }
}
