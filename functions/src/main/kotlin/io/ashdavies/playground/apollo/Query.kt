package io.ashdavies.playground.apollo

import com.apollographql.apollo.api.Response
import io.ashdavies.playground.github.ConferencesQuery
import io.ashdavies.playground.kotlin.requireNotNull

internal val Response<ConferencesQuery.Data>.entries: List<ConferencesQuery.Entry>
    get() = data
        ?.repository
        ?.conferences
        ?.asTree
        ?.entries
        ?.requireNoNulls()
        .requireNotNull { "Failed to retrieve entries from GitHub API" }

internal fun List<ConferencesQuery.Entry>.asBlobs(): List<ConferencesQuery.AsBlob> =
    map { it.data?.asBlob.requireNotNull { "Failed to retrieve blob from GitHub entry" } }

internal fun ConferencesQuery.AsBlob.requireOid(): String = requireNotNull(oid as String)
internal fun ConferencesQuery.AsBlob.requireText(): String = requireNotNull(text)