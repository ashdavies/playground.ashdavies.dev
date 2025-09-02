package dev.ashdavies.playground.events

import androidx.compose.ui.Modifier
import app.cash.paparazzi.Paparazzi
import dev.ashdavies.playground.tooling.MaterialPreviewTheme
import dev.ashdavies.playground.tooling.UnitTestResources
import dev.ashdavies.playground.tooling.upcomingEventsList
import org.junit.Rule
import kotlin.test.Test

internal class EventsDetailTests {

    private val eventsDetailUi = EventsDetailUi()

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                eventsDetailUi.Content(
                    state = EventsDetailScreen.State(
                        itemState = EventsDetailScreen.State.ItemState.Done(
                            item = UnitTestResources
                                .upcomingEventsList()
                                .first(),
                        ),
                        onBackPressed = { },
                    ),
                    modifier = Modifier,
                )
            }
        }
    }
}
