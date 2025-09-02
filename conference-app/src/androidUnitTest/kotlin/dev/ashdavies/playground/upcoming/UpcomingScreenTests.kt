package dev.ashdavies.playground.upcoming

import androidx.compose.ui.Modifier
import app.cash.paparazzi.Paparazzi
import dev.ashdavies.playground.tooling.MaterialPreviewTheme
import dev.ashdavies.playground.tooling.UnitTestResources
import dev.ashdavies.playground.tooling.upcomingEventsList
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import kotlin.test.Test

internal class UpcomingScreenTests {

    private val upcomingUi = UpcomingUi()

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                upcomingUi.Content(
                    state = UpcomingScreen.State(
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
