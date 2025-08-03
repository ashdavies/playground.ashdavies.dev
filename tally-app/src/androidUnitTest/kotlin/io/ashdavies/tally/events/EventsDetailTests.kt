package io.ashdavies.tally.events

import app.cash.paparazzi.Paparazzi
import io.ashdavies.tally.tooling.MaterialPreviewTheme
import io.ashdavies.tally.tooling.UnitTestResources
import io.ashdavies.tally.tooling.upcomingEventsList
import org.junit.Rule
import kotlin.test.Test

internal class EventsDetailTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventsDetailUi(
                    item = UnitTestResources
                        .upcomingEventsList()
                        .first(),
                    navigationIcon = { },
                )
            }
        }
    }
}
