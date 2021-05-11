package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.SourceOfTruth
import com.squareup.sqldelight.Query
import io.ashdavies.playground.database.ConferencesQueries
import io.ashdavies.playground.database.Conference as Surrogate

internal fun ConferencesSourceOfTruth(
    conferencesQueries: ConferencesQueries,
): SourceOfTruth<Unit, List<Surrogate>, List<Surrogate>> = SourceOfTruth.of(
    nonFlowReader = NonFlowReader(conferencesQueries),
    writer = Writer(conferencesQueries),
    delete = { conferencesQueries.deleteAllConferences() },
    deleteAll = { conferencesQueries.deleteAllConferences() },
)

private typealias NonFlowReader = suspend (Any) -> List<Surrogate>

private fun NonFlowReader(conferencesQueries: ConferencesQueries): NonFlowReader = {
    conferencesQueries
        .selectAllConferences()
        .executeAsList()
        .map(mapper(::Conference))
}

private typealias Writer = suspend (Any, List<Surrogate>) -> Unit

private fun Writer(conferencesQueries: ConferencesQueries): Writer = { _, it ->
    it.forEach(conferencesQueries.insertOrReplaceConference(it))
}

private fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> mapper(
    block: (T1, T2, T3, T4, T4, T5, T6, T6, T7, T8, T9) -> R
): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T12, T13) -> R = {
        t1, t2, t3, t4, t5, t6, t7, t8, t9, _, _, _, _ -> block(t1, t2, t3, t4, t5, t6, t7, t8, t9)
}
