package io.ashdavies.tally.home

import app.cash.paparazzi.Paparazzi
import io.ashdavies.tally.tooling.MaterialPreviewTheme
import org.junit.Rule
import kotlin.test.Test

internal class HomeBottomBarTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                HomeBottomBar()
            }
        }
    }
}
