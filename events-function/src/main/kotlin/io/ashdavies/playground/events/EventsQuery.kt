package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.invoke

private const val DEFAULT_ORDER_BY = "dateStart"
private const val DEFAULT_LIMIT = 50

public class EventsQuery(request: HttpRequest) {
    public val limit: Int by request { it?.toInt() ?: DEFAULT_LIMIT }
    public val orderBy: String by request { it ?: DEFAULT_ORDER_BY }
    public val startAt: String? by request { it }
}
