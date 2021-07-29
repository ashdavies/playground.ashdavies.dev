package io.ashdavies.playground.events

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get
import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.google.FirebaseFunction
import io.ashdavies.playground.google.firstIntQueryParameterOrDefault
import io.ashdavies.playground.google.firstStringQueryParameterOrDefault
import io.ashdavies.playground.graph
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val DEFAULT_LIMIT = 50

class EventsFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val startAt: String by request.firstStringQueryParameterOrDefault { today() }
        val limit: Int by request.firstIntQueryParameterOrDefault { DEFAULT_LIMIT }

        val store: Store<Unit, List<Event>> = StoreBuilder.from(
            sourceOfTruth = EventsSourceOfTruth(graph.collectionReference, startAt, limit),
            fetcher = Fetcher.of { graph.gitHubService.getEvents() },
        ).build()

        return Json.encodeToString(store.get(Unit))
    }
}

private fun today(): String = Clock.System.now()
    .toLocalDateTime(TimeZone.UTC)
    .run { LocalDate(year, month, dayOfMonth) }
    .toString()
