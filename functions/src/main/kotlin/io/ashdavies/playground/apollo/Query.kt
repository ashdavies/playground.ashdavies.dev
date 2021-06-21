package io.ashdavies.playground.apollo

import com.apollographql.apollo.api.Response
import io.ashdavies.playground.github.EventsQuery
import io.ashdavies.playground.kotlin.requireNotNull

internal val Response<EventsQuery.Data>.entries: List<EventsQuery.Entry>
    get() = data
        ?.events
        ?.conferences
        ?.asTree
        ?.entries
        ?.requireNoNulls()
        .requireNotNull { "Failed to retrieve entries from GitHub API" }

internal fun List<EventsQuery.Entry>.asBlobs(): List<EventsQuery.AsBlob> =
    map { it.data?.asBlob.requireNotNull { "Failed to retrieve blob from GitHub entry" } }

internal fun EventsQuery.AsBlob.requireOid(): String = requireNotNull(oid as String)
internal fun EventsQuery.AsBlob.requireText(): String = requireNotNull(text)
