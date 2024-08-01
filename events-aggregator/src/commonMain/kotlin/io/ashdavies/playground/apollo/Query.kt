package io.ashdavies.playground.apollo

import com.apollographql.apollo.api.ApolloResponse
import io.ashdavies.github.EventsQuery

internal val ApolloResponse<EventsQuery.Data>.entries: List<EventsQuery.Entry>
    get() = dataOrThrow()
        .let { it.repository ?: error("Failed to find repository by owner and repository name") }
        .let { it.events ?: error("Failed to find events as a Git object on $it") }
        .let { it.onTree ?: error("Failed to represent $it as a tree") }
        .let { it.entries ?: error("No entries found on type $it") }

internal fun List<EventsQuery.Entry>.asBlobs(): List<EventsQuery.OnBlob> =
    map { it.data?.onBlob ?: error("Failed to retrieve blob from GitHub entry") }

internal fun EventsQuery.OnBlob.requireOid(): String =
    requireNotNull(oid as? String) { "Events query result oid (\"$oid\") is not a String" }

internal fun EventsQuery.OnBlob.requireText(): String =
    requireNotNull(text) { "Events query Blob cannot be converted to string" }
