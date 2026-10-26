package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.ashdavies.asg.AsgConference
import dev.ashdavies.playground.tooling.MaterialThemeWrapper
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Composable
@PreviewLightDark
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiSuccessPreview() {
    EventListUi(
        state = EventListState.Success(
            itemList = Json
                .asgUpcomingConferences { it.toEventListStateSuccessItem() }
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
private fun EventListUiInitialPreview() {
    EventListUi(EventListState.Initial)
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

private fun AsgConference.toEventListStateSuccessItem() = EventListState.Success.Item(
    thumbnailImageSeed = Random.nextLong(),
    name = name,
    location = location,
    date = formatDateRange(dateStart, dateEnd),
    status = when {
        cfp != null -> EventListState.Success.Item.StatusBadge(
            containerColor = Color(red = 255, green = 216, blue = 228),
            contentColor = Color(red = 49, green = 17, blue = 29),
            icon = Icons.Outlined.Schedule,
            text = "CfP Announced",
        )

        else -> EventListState.Success.Item.StatusBadge(
            containerColor = Color(red = 230, green = 224, blue = 233),
            contentColor = Color(red = 73, green = 69, blue = 79),
            icon = Icons.Outlined.Block,
            text = "Not Announced",
        )
    },
    isBookmarked = Random.nextBoolean(),
)
