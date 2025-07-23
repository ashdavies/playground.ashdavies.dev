package io.ashdavies.cloud.operations

import com.google.cloud.firestore.CollectionReference
import io.ashdavies.asg.AsgConference
import io.ashdavies.asg.AsgService
import io.ashdavies.cloud.ApiConferenceFactory
import io.ashdavies.cloud.CollectionWriter
import io.ashdavies.cloud.await
import io.ashdavies.cloud.decodeFromSnapshot
import io.ashdavies.http.common.models.ApiConference
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
