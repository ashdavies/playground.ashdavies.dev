package dev.ashdavies.playground.event

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.serialization.json.Json

internal class EventPreviewParameterProvider : PreviewParameterProvider<Event> {
    override val values = Json
        .upcomingEvents()
        .asSequence()
}
