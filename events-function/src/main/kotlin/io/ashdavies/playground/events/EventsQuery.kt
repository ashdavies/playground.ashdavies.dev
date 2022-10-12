package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.invoke

private const val DEFAULT_ORDER_BY = "dateStart"
private const val DEFAULT_LIMIT = 50

public class EventsQuery(request: HttpRequest) {
    val limit: Int by request { it?.toInt() ?: DEFAULT_LIMIT }
    val orderBy: String by request { it ?: DEFAULT_ORDER_BY }
    val startAt: String? by request { it }
}
