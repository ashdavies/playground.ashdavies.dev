package io.ashdavies.lanyard.events

import app.cash.paparazzi.Paparazzi
import io.ashdavies.lanyard.tooling.MaterialPreviewTheme
import io.ashdavies.lanyard.upcoming.UpcomingEventsPane
import io.ashdavies.lanyard.upcoming.UpcomingEventsScreen
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import kotlin.test.Test

private val DroidconEvents = listOf(AndroidMakers, DroidconBerlin, DroidconLondon)

internal class EventsListTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                UpcomingEventsPane(
                    state = UpcomingEventsScreen.State(
                        itemList = DroidconEvents.toImmutableList(),
                        selectedIndex = null,
                        isRefreshing = false,
                        errorMessage = null,
                        eventSink = { },
                    ),
                    onClick = { },
                )
            }
        }
    }
}
