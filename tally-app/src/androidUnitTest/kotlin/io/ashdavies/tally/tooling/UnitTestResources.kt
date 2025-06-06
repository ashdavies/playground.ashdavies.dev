package io.ashdavies.tally.tooling

import io.ashdavies.asg.AsgConference
import io.ashdavies.tally.events.Event
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileInputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal object UnitTestResources {
    val upcomingEventsList: List<Event> by lazy(LazyThreadSafetyMode.NONE) {
        decodeFromResource<List<AsgConference>>("upcoming.json").map {
            Event(
                id = randomUuidAsString(),
                name = it.name,
                website = it.website,
                location = it.location,
                imageUrl = null,
                status = it.status,
                online = it.online,
                dateStart = it.dateStart,
                dateEnd = it.dateEnd,
                cfpStart = it.cfp?.start,
                cfpEnd = it.cfp?.end,
                cfpSite = it.cfp?.site,
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
private inline fun <reified T> decodeFromResource(name: String): T {
    return Json.decodeFromStream(FileInputStream(File("src/androidUnitTest/resources/$name")))
}

@OptIn(ExperimentalUuidApi::class)
private fun randomUuidAsString() = "${Uuid.random()}"
