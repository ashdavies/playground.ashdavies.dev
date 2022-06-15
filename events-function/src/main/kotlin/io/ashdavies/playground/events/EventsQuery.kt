package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.firstQueryParameter

private const val DEFAULT_ORDER_BY = "dateStart"
private const val DEFAULT_LIMIT = 50

public class EventsQuery(request: HttpRequest) {
    val limit: Int by request.firstQueryParameter { it?.toInt() ?: DEFAULT_LIMIT }
    val orderBy: String by request.firstQueryParameter { it ?: DEFAULT_ORDER_BY }
    val startAt: String? by request.firstQueryParameter { it }
}
