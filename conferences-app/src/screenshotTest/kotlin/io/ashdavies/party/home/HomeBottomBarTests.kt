package io.ashdavies.party.home

import androidx.compose.runtime.Composable
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight

internal class HomeBottomBarTests {

    @Composable
    @PreviewDayNight
    private fun HomeBottomSheetPreview() {
        MaterialPreviewTheme {
            HomeBottomBar()
        }
    }
}
