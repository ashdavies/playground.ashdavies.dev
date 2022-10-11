package io.ashdavies.playground.events

import com.google.cloud.functions.HttpFunction
import io.ashdavies.playground.EventsSerializer
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.rememberDocumentProvider
import io.ashdavies.playground.google.DocumentProvider
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private const val COLLECTION_PATH = "events"

internal class EventsFunction : HttpFunction by HttpApplication({
    val provider: DocumentProvider = rememberDocumentProvider(COLLECTION_PATH)
    val query = EventsQuery(LocalHttpRequest.current)
    val reader = EventsReader(provider, query)

    // VerifiedHttpEffect
    HttpEffect(COLLECTION_PATH) {
        Json.encodeToString(ListSerializer(EventsSerializer), reader())
    }
})
