package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight

internal class EventsDetailTests {

    @Composable
    @PreviewDayNight
    private fun EventsDetailPreview() {
        MaterialPreviewTheme {
            EventsDetailPane(
                event = DroidconBerlin,
                onBackClick = { },
            )
        }
    }
}
