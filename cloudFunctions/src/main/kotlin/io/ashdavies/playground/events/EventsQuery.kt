package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.properties.ReadOnlyProperty

private const val DEFAULT_ORDER_BY = "dateStart"
private const val DEFAULT_LIMIT = 50

class EventsQuery(request: HttpRequest) {
    val limit: Int by request.firstQueryParameter { it?.toInt() ?: DEFAULT_LIMIT }
    val orderBy: String by request.firstQueryParameter { it ?: DEFAULT_ORDER_BY }
    val startAt: String by request.firstQueryParameter { it ?: today() }
}

private fun today(): String = Clock.System.now()
    .toLocalDateTime(TimeZone.UTC)
    .run { LocalDate(year, month, dayOfMonth) }
    .toString()

private fun <T : Any> HttpRequest.firstQueryParameter(supplier: (String?) -> T) =
    ReadOnlyProperty<Any?, T> { _, property ->
        getFirstQueryParameter(property.name)
            .map { supplier(it) }
            .orElseGet { supplier(null) }
    }
