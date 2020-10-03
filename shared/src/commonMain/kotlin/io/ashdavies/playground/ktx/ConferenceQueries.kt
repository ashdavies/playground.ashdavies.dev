package io.ashdavies.playground.ktx

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import kotlinx.coroutines.flow.Flow

fun ConferencesQueries.readByName(
    name: String
): Flow<List<Conference>> = selectByName(name)
    .asFlow()
    .mapToList()

suspend fun ConferencesQueries.writeAll(
    key: String,
    conferences: Iterable<Conference>
) = conferences.forEach(::insertOrReplace)
