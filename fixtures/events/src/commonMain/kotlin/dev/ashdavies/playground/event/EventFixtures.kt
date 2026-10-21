package dev.ashdavies.playground.event

import dev.ashdavies.asg.AsgConference
import dev.ashdavies.cloud.uuid
import kotlinx.serialization.json.Json
import playground.fixtures.events.fixtures.UpcomingJson
import dev.ashdavies.playground.event.Event as DatabaseEvent

public fun <T : Any> Json.asgUpcomingConferences(
    transform: (AsgConference) -> T,
): List<T> = decodeFromString<List<AsgConference>>(
    string = UpcomingJson,
).map(transform)

public fun Json.upcomingEvents(): List<DatabaseEvent> = asgUpcomingConferences { item ->
    DatabaseEvent(
        uuid = item.uuid(),
        name = item.name,
        website = item.website,
        location = item.location,
        dateStart = item.dateStart,
        dateEnd = item.dateEnd,
    )
}
