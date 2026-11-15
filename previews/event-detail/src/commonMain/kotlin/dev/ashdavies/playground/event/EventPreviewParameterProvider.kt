package dev.ashdavies.playground.event

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.ashdavies.http.common.models.toDatabaseEvent

internal class EventPreviewParameterProvider : PreviewParameterProvider<Event> {
    override val values = EventFixtures
        .upcoming { it.toDatabaseEvent() }
        .asSequence()
}
