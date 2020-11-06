package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.SourceOfTruth.Companion.of
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferencesQueries

class ConferencesSourceOfTruth(
    conferencesQueries: ConferencesQueries,
) : SourceOfTruth<Any, List<Conference>, List<Conference>> by of(
    nonFlowReader = { conferencesQueries.selectAll().executeAsList() },
    writer = { _, it -> it.forEach(conferencesQueries::insertOrReplace) },
    deleteAll = { conferencesQueries.deleteAll() },
    delete = { conferencesQueries.deleteAll() },
)
