package io.ashdavies.cloud.operations

import io.ashdavies.aggregator.AsgConference
import io.ashdavies.aggregator.AsgService
import io.ashdavies.cloud.CollectionQuery
import io.ashdavies.cloud.CollectionReader
import io.ashdavies.cloud.CollectionWriter
import io.ashdavies.cloud.DocumentProvider
import io.ashdavies.cloud.Identifier
import io.ashdavies.cloud.invoke
import io.ashdavies.http.common.models.Event
import io.ashdavies.http.common.models.EventCfp
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

internal class AggregateEventsOperation(
    private val documentProvider: DocumentProvider,
    private val collectionWriter: CollectionWriter<Event>,
    private val asgService: AsgService,
    private val identifier: Identifier<AsgConference>,
) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val reader = CollectionReader<Event>(documentProvider, CollectionQuery(limit = 0))
        collectionWriter(reader(), asgService { it.toEvent(identifier(it)) })
        call.respond(HttpStatusCode.OK)
    }
}

private fun AsgConference.toEvent(id: String) = Event(
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
