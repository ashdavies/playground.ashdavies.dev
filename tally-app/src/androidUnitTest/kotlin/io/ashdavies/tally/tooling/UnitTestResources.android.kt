package io.ashdavies.tally.tooling

import io.ashdavies.asg.AsgConference
import io.ashdavies.tally.events.Event as DatabaseEvent

internal fun UnitTestResources.upcomingEventsList(): List<DatabaseEvent> {
    return decodeFromResource<List<AsgConference>>("androidUnitTest", "upcoming.json").mapIndexed { index, item ->
        DatabaseEvent(
            id = index.toLong(),
            name = item.name,
            website = item.website,
            location = item.location,
            imageUrl = null,
            status = item.status,
            online = item.online,
            dateStart = item.dateStart,
            dateEnd = item.dateEnd,
            cfpStart = item.cfp?.start,
            cfpEnd = item.cfp?.end,
            cfpSite = item.cfp?.site,
        )
    }
}
