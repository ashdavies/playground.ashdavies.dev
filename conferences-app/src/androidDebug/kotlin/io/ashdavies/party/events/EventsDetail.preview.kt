package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight

@Composable
@PreviewDayNight
internal fun EventsDetailPreview() {
    MaterialPreviewTheme {
        EventsDetail(DroidconBerlin)
    }
}
