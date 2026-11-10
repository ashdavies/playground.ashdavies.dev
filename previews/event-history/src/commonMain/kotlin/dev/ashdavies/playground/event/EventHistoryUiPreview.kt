package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.ashdavies.asg.AsgConference
import dev.ashdavies.playground.event.history.EventHistoryState
import dev.ashdavies.playground.event.history.EventHistoryUi
import dev.ashdavies.playground.tooling.MaterialThemeWrapper
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.json.Json
import kotlin.uuid.Uuid

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventHistoryUiPreview() {
    EventHistoryUi(
        state = EventHistoryState(
            itemList = Json
                .asgUpcomingConferences { it.toEventHistoryStateItem() }
                .toPersistentList(),
            eventSink = { },
        ),
    )
}

private fun AsgConference.toEventHistoryStateItem() = EventHistoryState.Item(
    thumbnailImageSeed = 0L,
    uuid = Uuid.random(),
    name = name,
    year = "${dateStart.year}",
)
