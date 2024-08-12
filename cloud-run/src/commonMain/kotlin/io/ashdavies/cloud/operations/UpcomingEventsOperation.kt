package io.ashdavies.cloud.operations

import io.ashdavies.cloud.CollectionQuery
import io.ashdavies.cloud.CollectionReader
import io.ashdavies.cloud.DocumentProvider
import io.ashdavies.http.common.models.Event
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

internal object UpcomingEventsDefaults {
    const val ORDER_BY: String = "dateStart"
    const val LIMIT: Int = 50
}

internal class UpcomingEventsOperation(
    private val documentProvider: DocumentProvider,
) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val startAt = call.request.queryParameters["startAt"] ?: todayAsString()
        val limit = call.request.queryParameters["limit"]?.toInt() ?: UpcomingEventsDefaults.LIMIT

        val query = CollectionQuery(UpcomingEventsDefaults.ORDER_BY, startAt, limit)
        val reader = CollectionReader<Event>(documentProvider, query)

        call.respond(reader(Event.serializer()))
    }
}

private fun todayAsString(): String = Clock.System
    .todayIn(TimeZone.currentSystemDefault())
    .toString()
