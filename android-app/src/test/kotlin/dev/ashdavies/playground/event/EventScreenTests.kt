package dev.ashdavies.playground.event

import app.cash.paparazzi.Paparazzi
import dev.ashdavies.asg.AsgConference
import dev.ashdavies.playground.event.detail.EventDetailState
import dev.ashdavies.playground.event.detail.EventsDetailUi
import dev.ashdavies.playground.event.grid.EventGridState
import dev.ashdavies.playground.event.grid.EventGridUi
import dev.ashdavies.playground.tooling.MaterialPreviewTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.Rule
import org.junit.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class EventScreenTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun eventList() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventListUi(
                    state = EventListState(
                        itemList = Json
                            .upcomingEvents()
                            .toImmutableList(),
                        selectedIndex = null,
                        isRefreshing = false,
                        errorMessage = null,
                        eventSink = { },
                    ),
                )
            }
        }
    }

    @Test
    fun eventGrid() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventGridUi(
                    state = EventGridState(
                        itemList = Json
                            .upcomingEvents()
                            .map(Event::toEventGridStateItem)
                            .toImmutableList(),
                        eventSink = { },
                    ),
                )
            }
        }
    }

    @Test
    fun eventDetail() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventsDetailUi(
                    state = EventDetailState(
                        itemState = EventDetailState.ItemState.Done(
                            item = Json.upcomingEvents().first(),
                        ),
                        onBackPressed = { },
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
private fun Event.toEventGridStateItem(): EventGridState.Item {
    val year = LocalDate.parse(dateStart).year
    return EventGridState.Item(
        uuid = "${Uuid.random()}",
        title = "$name $year",
        subtitle = location,
        group = "$year",
        attended = false,
    )
}

@OptIn(ExperimentalSerializationApi::class)
internal fun Json.upcomingEvents(): List<Event> = decodeFromStream<List<AsgConference>>(
    stream = Event::class.java
        .getResource("/upcoming.json")
        .let(::requireNotNull)
        .openStream(),
).mapIndexed { index, item ->
    item.toEvent(index.toLong())
}

private fun AsgConference.toEvent(id: Long) = Event(
    id = id,
    name = name,
    website = website,
    location = location,
    imageUrl = null,
    status = status,
    online = online,
    dateStart = dateStart,
    dateEnd = dateEnd,
    cfpStart = cfp?.start,
    cfpEnd = cfp?.end,
    cfpSite = cfp?.site,
)
