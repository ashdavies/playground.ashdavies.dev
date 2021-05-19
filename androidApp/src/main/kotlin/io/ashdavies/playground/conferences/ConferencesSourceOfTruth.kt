package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.SourceOfTruth
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.database.ConferencesQueries

internal fun ConferencesSourceOfTruth(
    conferencesQueries: ConferencesQueries,
): SourceOfTruth<Unit, List<Conference>, List<Conference>> = SourceOfTruth.of(
    nonFlowReader = NonFlowReader(conferencesQueries),
    writer = Writer(conferencesQueries),
    delete = { conferencesQueries.deleteAll() },
    deleteAll = { conferencesQueries.deleteAll() },
)

private typealias NonFlowReader = suspend (Any) -> List<Conference>

private fun NonFlowReader(conferencesQueries: ConferencesQueries): NonFlowReader = {
    conferencesQueries
        .selectAll()
        .executeAsList()
}

private typealias Writer = suspend (Any, List<Conference>) -> Unit

private fun Writer(conferencesQueries: ConferencesQueries): Writer = { _, it ->
    it.forEach(conferencesQueries::insertOrReplace)
}
