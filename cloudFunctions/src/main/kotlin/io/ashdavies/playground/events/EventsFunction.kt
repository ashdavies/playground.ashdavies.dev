package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.database.EventsSerializer
import io.ashdavies.playground.google.FirebaseFunction
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

internal class EventsFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val eventsReader = EventsReader(
            request = EventsQuery(request),
            provider = DocumentProvider(),
        )

        return Json.encodeToString(
            serializer = ListSerializer(EventsSerializer),
            value = eventsReader()
        )
    }
}
