package io.ashdavies.party.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight
import kotlinx.collections.immutable.persistentListOf

@Composable
@PreviewDayNight
@OptIn(ExperimentalFoundationApi::class)
internal fun GalleryGridPreview() {
    MaterialPreviewTheme {
        GalleryGrid(
            itemList = persistentListOf(
                galleryScreenStateItem(),
                galleryScreenStateItem(isSelected = true),
                galleryScreenStateItem(state = SyncState.SYNCING),
                galleryScreenStateItem(state = SyncState.SYNCED),
            ),
            onExpand = { },
            onSelect = { },
            isSelecting = true,
        )
    }
}

@Composable
@PreviewDayNight
internal fun GalleryBottomSheetPreview() {
    MaterialPreviewTheme {
        GallerySheetContent(eventSink = { })
    }
}

private fun galleryScreenStateItem(
    name: String = "Sample Image",
    isSelected: Boolean = false,
    state: SyncState = SyncState.NOT_SYNCED,
) = GalleryScreen.State.StandardItem(
    title = name,
    imageModel = null,
    isSelected = isSelected,
    state = state,
)
