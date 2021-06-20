package io.ashdavies.playground.events

import com.dropbox.android.external.store4.SourceOfTruth
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.database.EventsQueries

internal fun EventsSourceOfTruth(
    eventsQueries: EventsQueries,
): SourceOfTruth<Unit, List<Event>, List<Event>> = SourceOfTruth.of(
    nonFlowReader = NonFlowReader(eventsQueries),
    writer = Writer(eventsQueries),
    delete = { eventsQueries.deleteAll() },
    deleteAll = { eventsQueries.deleteAll() },
)

private typealias NonFlowReader = suspend (Any) -> List<Event>

private fun NonFlowReader(eventsQueries: EventsQueries): NonFlowReader = {
    eventsQueries
        .selectAll()
        .executeAsList()
}

private typealias Writer = suspend (Any, List<Event>) -> Unit

private fun Writer(eventsQueries: EventsQueries): Writer = { _, it ->
    it.forEach(eventsQueries::insertOrReplace)
}
