package io.ashdavies.tally.events

import app.cash.paparazzi.Paparazzi
import io.ashdavies.tally.tooling.MaterialPreviewTheme
import io.ashdavies.tally.upcoming.UpcomingPane
import io.ashdavies.tally.upcoming.UpcomingScreen
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
                UpcomingPane(
                    state = UpcomingScreen.State(
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
