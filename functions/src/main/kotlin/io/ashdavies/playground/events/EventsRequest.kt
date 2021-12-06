package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.google.firstIntQueryParameterOrDefault
import io.ashdavies.playground.google.firstStringQueryParameterOrDefault
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val DEFAULT_LIMIT = 50

internal class EventsRequest(request: HttpRequest) {
    val startAt: String by request.firstStringQueryParameterOrDefault { today() }
    val limit: Int by request.firstIntQueryParameterOrDefault { DEFAULT_LIMIT }
}

private fun today(): String = Clock.System.now()
    .toLocalDateTime(TimeZone.UTC)
    .run { LocalDate(year, month, dayOfMonth) }
    .toString()
