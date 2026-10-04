package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.ashdavies.playground.tooling.MaterialThemeWrapper
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.json.Json

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiSuccessPreview() {
    EventListUi(
        state = EventListState.Success(
            itemList = Json
                .upcomingEvents()
                .map { it.toEventListStateSuccessItem() }
                .toPersistentList(),
            searchResults = persistentListOf(),
            isRefreshing = false,
            eventSink = { },
        ),
    )
}

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiFailurePreview() {
    EventListUi(
        state = EventListState.Failure(
            icon = Icons.Outlined.CloudOff,
            message = "Something went wrong",
        ),
    )
}
