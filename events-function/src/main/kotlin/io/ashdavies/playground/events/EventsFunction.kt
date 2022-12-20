package io.ashdavies.playground.events

import com.google.cloud.functions.HttpFunction
import io.ashdavies.compose.VerifiedHttpEffect
import io.ashdavies.playground.EventsSerializer
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpConfig
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.rememberDocumentProvider
import io.ashdavies.playground.google.DocumentProvider
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private const val COLLECTION_PATH = "events"

internal class EventsFunction : HttpFunction by HttpApplication(HttpConfig.Get, {
    val provider: DocumentProvider = rememberDocumentProvider(COLLECTION_PATH)
    val query = EventsQuery(LocalHttpRequest.current)
    val reader = EventsReader(provider, query)

    VerifiedHttpEffect {
        Json.encodeToString(ListSerializer(EventsSerializer), reader())
    }
},)
