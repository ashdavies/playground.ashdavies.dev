package dev.ashdavies.playground.tooling

import dev.ashdavies.asg.AsgConference
import dev.ashdavies.playground.event.Event

internal fun UnitTestResources.upcomingEventsList(): List<Event> {
    return decodeFromResource<List<AsgConference>>("androidUnitTest", "upcoming.json").mapIndexed { index, item ->
        Event(
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
