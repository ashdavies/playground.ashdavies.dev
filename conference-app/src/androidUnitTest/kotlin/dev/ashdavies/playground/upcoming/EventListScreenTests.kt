package dev.ashdavies.playground.upcoming

import androidx.compose.ui.Modifier
import app.cash.paparazzi.Paparazzi
import dev.ashdavies.playground.event.EventListState
import dev.ashdavies.playground.event.EventListUi
import dev.ashdavies.playground.tooling.MaterialPreviewTheme
import dev.ashdavies.playground.tooling.UnitTestResources
import dev.ashdavies.playground.tooling.upcomingEventsList
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import kotlin.test.Test

internal class EventListScreenTests {

    private val eventListUi = EventListUi()

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                eventListUi.Content(
                    state = EventListState(
                        itemList = UnitTestResources
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
}
