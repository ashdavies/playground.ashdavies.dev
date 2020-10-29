package io.ashdavies.playground.ktx

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import kotlinx.coroutines.flow.Flow

fun ConferencesQueries.readAll(): Flow<List<Conference>> =
    selectAll()
        .asFlow()
        .mapToList()

fun ConferencesQueries.writeAll(conferences: Iterable<Conference>) =
    conferences.forEach { insertOrReplace(it) }
