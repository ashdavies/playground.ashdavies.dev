package io.ashdavies.party.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import io.ashdavies.party.past.GalleryGrid
import io.ashdavies.party.past.GalleryScreen
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight
import kotlinx.collections.immutable.persistentListOf

internal class GalleryScreenTests {

    @Composable
    @PreviewDayNight
    @OptIn(ExperimentalFoundationApi::class)
    private fun GalleryGridPreview() {
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
    private fun GalleryBottomSheetPreview() {
        MaterialPreviewTheme {
            GallerySheetContent(eventSink = { })
        }
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
