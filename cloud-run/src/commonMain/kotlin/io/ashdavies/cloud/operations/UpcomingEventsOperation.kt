package io.ashdavies.cloud.operations

import com.google.cloud.firestore.CollectionReference
import io.ashdavies.cloud.await
import io.ashdavies.cloud.decodeFromSnapshot
import io.ashdavies.http.common.models.ApiConference
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private object UpcomingEventsDefaults {
    const val ORDER_BY = "dateStart"
    const val LIMIT = 50
}

internal class UpcomingEventsOperation(
    private val collectionReference: CollectionReference,
) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val snapshot = collectionReference
            .orderBy(UpcomingEventsDefaults.ORDER_BY)
            .startAt(call.request.queryParameters["startAt"] ?: todayAsString())
            .limit(call.request.queryParameters["limit"]?.toInt() ?: UpcomingEventsDefaults.LIMIT)
            .await()

        call.respond(Json.decodeFromSnapshot<ApiConference>(snapshot))
    }
}

@OptIn(ExperimentalTime::class)
private fun todayAsString(): String = Clock.System
    .todayIn(TimeZone.currentSystemDefault())
    .toString()
