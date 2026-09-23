package dev.ashdavies.playground.event

import dev.ashdavies.asg.AsgConference
import kotlinx.serialization.json.Json
import playground.fixtures.events.fixtures.UpcomingJson

public fun Json.upcomingEvents(): List<Event> = decodeFromString<List<AsgConference>>(
    string = UpcomingJson,
).mapIndexed { index, item ->
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
