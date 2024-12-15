package io.ashdavies.party.home

import androidx.compose.runtime.Composable
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight

internal class HomeBottomBarTest {

    @Composable
    @PreviewDayNight
    private fun HomeBottomSheetPreview() {
        MaterialPreviewTheme {
            HomeBottomBar()
        }
    }
}
