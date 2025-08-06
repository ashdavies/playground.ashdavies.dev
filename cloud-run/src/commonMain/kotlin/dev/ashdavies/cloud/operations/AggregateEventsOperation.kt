package dev.ashdavies.cloud.operations

import com.google.cloud.firestore.CollectionReference
import dev.ashdavies.asg.AsgService
import dev.ashdavies.cloud.ApiConferenceFactory
import dev.ashdavies.cloud.CollectionWriter
import dev.ashdavies.cloud.await
import dev.ashdavies.cloud.decodeFromSnapshot
import io.ashdavies.http.common.models.ApiConference
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json

private object AggregateEventsDefaults {
    const val ORDER_BY = "dateStart"
}

internal class AggregateEventsOperation(
    private val collectionReference: CollectionReference,
    private val collectionWriter: CollectionWriter<ApiConference>,
    private val asgService: AsgService,
    private val apiConferenceFactory: ApiConferenceFactory,
) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val snapshot = collectionReference
            .orderBy(AggregateEventsDefaults.ORDER_BY)
            .await()

        collectionWriter(
            oldValue = Json.decodeFromSnapshot(snapshot),
            newValue = asgService(apiConferenceFactory::invoke),
        )

        call.respond(HttpStatusCode.OK)
    }
}
