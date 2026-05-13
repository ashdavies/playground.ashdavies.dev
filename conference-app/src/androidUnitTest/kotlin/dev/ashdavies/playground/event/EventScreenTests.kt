package dev.ashdavies.playground.event

import androidx.compose.ui.Modifier
import app.cash.paparazzi.Paparazzi
import dev.ashdavies.asg.AsgConference
import dev.ashdavies.playground.event.grid.EventGridState
import dev.ashdavies.playground.event.grid.EventGridUi
import dev.ashdavies.playground.events.EventDetailState
import dev.ashdavies.playground.events.EventsDetailUi
import dev.ashdavies.playground.tooling.MaterialPreviewTheme
import dev.ashdavies.playground.tooling.decodeFromResource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.junit.Rule
import kotlin.test.Test
import kotlin.uuid.Uuid

internal class EventScreenTests {

    private val eventsDetailUi = EventsDetailUi()

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun eventList() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventListUi(
                    state = EventListState(
                        itemList = Json
                            .upcomingEventsList()
                            .toImmutableList(),
                        selectedIndex = null,
                        isRefreshing = false,
                        errorMessage = null,
                        eventSink = { },
                    ),
                    modifier = Modifier,
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
                            .upcomingEventsList()
                            .map { it.toEventGridStateItem() }
                            .toImmutableList(),
                        eventSink = { },
                    ),
                    modifier = Modifier,
                )
            }
        }
    }

    @Test
    fun eventDetail() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                eventsDetailUi.Content(
                    state = EventDetailState(
                        itemState = EventDetailState.ItemState.Done(
                            item = Json
                                .upcomingEventsList()
                                .first(),
                        ),
                        onBackPressed = { },
                    ),
                    modifier = Modifier.Companion,
                )
            }
        }
    }
}

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

internal fun Json.upcomingEventsList(): List<Event> {
    return decodeFromResource<List<AsgConference>>("androidUnitTest", "upcoming.json")
        .mapIndexed { index, item -> item.toEvent(index.toLong()) }
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
