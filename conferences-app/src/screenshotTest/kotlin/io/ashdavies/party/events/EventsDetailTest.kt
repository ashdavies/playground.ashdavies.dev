package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight

internal class EventsDetailTest {

    @Composable
    @PreviewDayNight
    fun EventsDetailPreview() {
        MaterialPreviewTheme {
            EventsDetail(DroidconBerlin)
        }
    }
}
