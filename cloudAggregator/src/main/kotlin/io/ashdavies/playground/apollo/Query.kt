package io.ashdavies.playground.apollo

import com.apollographql.apollo.api.Response
import io.ashdavies.playground.github.EventsQuery

internal val Response<EventsQuery.Data>.entries: List<EventsQuery.Entry>
    get() = requireNotNull(data) { "Apollo operation \"$this\" execution failed" }
        .let { it.repository requireNotNull "Failed to find repository by owner and repository name" }
        .let { it.events requireNotNull "Failed to find events as a Git object on ${it.__typename}" }
        .let { it.asTree requireNotNull "Failed to represent ${it.__typename} as a tree" }
        .let { it.entries requireNotNull "No entries found on type ${it.__typename}" }

internal fun List<EventsQuery.Entry>.asBlobs(): List<EventsQuery.AsBlob> =
    map { it.data?.asBlob requireNotNull "Failed to retrieve blob from GitHub entry" }

internal fun EventsQuery.AsBlob.requireOid(): String =
    requireNotNull(oid as? String) { "Events query result oid (\"$oid\") is not a String" }

internal fun EventsQuery.AsBlob.requireText(): String =
    requireNotNull(text) { "Events query Blob cannot be converted to string" }

private infix fun <T : Any> T?.requireNotNull(message: String): T =
    requireNotNull(this) { message }
