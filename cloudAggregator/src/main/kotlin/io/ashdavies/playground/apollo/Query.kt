package io.ashdavies.playground.apollo

import com.apollographql.apollo.api.Response
import io.ashdavies.playground.github.EventsQuery

internal val Response<EventsQuery.Data>.entries: List<EventsQuery.Entry>
    get() = data // TODO Require each item with helpful message
        ?.repository // EventsQuery.Repository?
        ?.events // EventsQuery.Events?
        ?.asTree // EventsQuery.AsTree?
        ?.entries // List<EventsQuery>?
        ?: throw IllegalArgumentException("Failed to retrieve entries from GitHub API")

internal fun List<EventsQuery.Entry>.asBlobs(): List<EventsQuery.AsBlob> = map {
    requireNotNull(it.data?.asBlob) { "Failed to retrieve blob from GitHub entry" }
}

internal fun EventsQuery.AsBlob.requireOid(): String = requireNotNull(oid as String)

internal fun EventsQuery.AsBlob.requireText(): String = requireNotNull(text)
