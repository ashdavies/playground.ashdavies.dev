package io.ashdavies.tally.tooling

import io.ashdavies.asg.AsgConference
import io.ashdavies.tally.events.Event
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileInputStream

internal object UnitTestResources {
    val upcomingEventsList: List<Event> by lazy(LazyThreadSafetyMode.NONE) {
        decodeFromResource<List<AsgConference>>("upcoming.json").mapIndexed { index, item ->
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
}

@OptIn(ExperimentalSerializationApi::class)
private inline fun <reified T> decodeFromResource(name: String): T {
    return Json.decodeFromStream(FileInputStream(File("src/androidUnitTest/resources/$name")))
}
