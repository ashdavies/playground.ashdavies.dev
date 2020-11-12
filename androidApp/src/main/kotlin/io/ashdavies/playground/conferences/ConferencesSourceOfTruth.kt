@file:Suppress("FunctionName")

package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.SourceOfTruth
import io.ashdavies.playground.database.ConferencesQueries
import io.ashdavies.playground.database.Conference as Surrogate

internal fun ConferencesSourceOfTruth(
    conferencesQueries: ConferencesQueries,
): SourceOfTruth<Any, List<Surrogate>, List<Surrogate>> = SourceOfTruth.of(
    nonFlowReader = NonFlowReader(conferencesQueries),
    writer = Writer(conferencesQueries),
    delete = { conferencesQueries.deleteAll() },
    deleteAll = { conferencesQueries.deleteAll() },
)

private typealias NonFlowReader = suspend (Any) -> List<Surrogate>

private fun NonFlowReader(conferencesQueries: ConferencesQueries): NonFlowReader = {
    conferencesQueries
        .selectAll()
        .executeAsList()
}

private typealias Writer = suspend (Any, List<Surrogate>) -> Unit

private fun Writer(conferencesQueries: ConferencesQueries): Writer = { _, it ->
    it.forEach(conferencesQueries::insertOrReplace)
}
