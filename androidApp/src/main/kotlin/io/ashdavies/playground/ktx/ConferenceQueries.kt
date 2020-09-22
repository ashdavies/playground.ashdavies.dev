package io.ashdavies.playground.ktx

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries
import kotlinx.coroutines.flow.Flow

internal fun ConferencesQueries.selectAllAsFlowList(): Flow<List<Conference>> {
    return selectAll()
        .asFlow()
        .mapToList()
}

internal fun ConferencesQueries.insertOrReplaceAll(values: List<Conference>) {
    values.forEach(::insertOrReplace)
}
