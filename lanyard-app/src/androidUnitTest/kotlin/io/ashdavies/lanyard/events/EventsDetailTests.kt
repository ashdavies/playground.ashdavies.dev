package io.ashdavies.lanyard.events

import app.cash.paparazzi.Paparazzi
import io.ashdavies.lanyard.tooling.MaterialPreviewTheme
import org.junit.Rule
import kotlin.test.Test

internal class EventsDetailTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventsDetailPane(
                    item = DroidconBerlin,
                    onBackClick = { },
                )
            }
        }
    }
}
