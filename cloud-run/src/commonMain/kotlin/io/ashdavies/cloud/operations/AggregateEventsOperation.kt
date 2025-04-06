package io.ashdavies.cloud.operations

import com.google.cloud.firestore.CollectionReference
import io.ashdavies.aggregator.AsgConference
import io.ashdavies.aggregator.AsgService
import io.ashdavies.cloud.CollectionWriter
import io.ashdavies.cloud.Identifier
import io.ashdavies.cloud.await
import io.ashdavies.cloud.decodeFromSnapshot
import io.ashdavies.http.common.models.Event
import io.ashdavies.http.common.models.EventCfp
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json

private object AggregateEventsDefaults {
    const val ORDER_BY = "dateStart"
}

internal class AggregateEventsOperation(
    private val collectionReference: CollectionReference,
    private val collectionWriter: CollectionWriter<Event>,
    private val asgService: AsgService,
    private val identifier: Identifier<AsgConference>,
) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val snapshot = collectionReference
            .orderBy(AggregateEventsDefaults.ORDER_BY)
            .await()

        collectionWriter(
            oldValue = Json.decodeFromSnapshot(snapshot),
            newValue = asgService { it.toEvent(identifier(it), null) },
        )

        call.respond(HttpStatusCode.OK)
    }
}

private fun AsgConference.toEvent(
    id: String,
    imageUrl: String?,
) = Event(
    id = id,
    name = name,
    website = website,
    location = location,
    imageUrl = imageUrl,
    dateStart = dateStart,
    dateEnd = dateEnd,
    status = status,
    online = online,
    cfp = cfp?.toEventCfp(),
)

private fun AsgConference.Cfp.toEventCfp() = EventCfp(
    start = start,
    end = end,
    site = site,
)
