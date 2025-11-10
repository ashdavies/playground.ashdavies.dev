package dev.ashdavies.cloud.events

import com.google.cloud.firestore.CollectionReference
import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.cloud.decodeFromSnapshot
import dev.ashdavies.cloud.google.await
import dev.ashdavies.http.common.models.ApiConference
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.binding
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val DEFAULT_ORDER_BY = "dateStart"
private const val DEFAULT_LIMIT = 50

@ContributesIntoSet(AppScope::class, binding<CloudRunRoute>())
internal class UpcomingEventsRoute @Inject constructor(
    @param:Named("events") private val collectionReference: CollectionReference,
) : CloudRunRoute {

    override fun Routing.invoke() = get("/events/upcoming") {
        val snapshot = collectionReference
            .orderBy(DEFAULT_ORDER_BY)
            .startAt(call.request.queryParameters["startAt"] ?: todayAsString())
            .limit(call.request.queryParameters["limit"]?.toIntOrNull() ?: DEFAULT_LIMIT)
            .await()

        call.respond(Json.decodeFromSnapshot<ApiConference>(snapshot))
    }
}

@OptIn(ExperimentalTime::class)
private fun todayAsString(): String = Clock.System
    .todayIn(TimeZone.currentSystemDefault())
    .toString()
