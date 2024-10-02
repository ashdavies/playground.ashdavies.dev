package io.ashdavies.party.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import io.ashdavies.party.tooling.PreviewDayNight
import kotlinx.collections.immutable.persistentListOf

@Composable
@PreviewDayNight
@OptIn(ExperimentalFoundationApi::class)
internal fun GalleryGridPreview() {
    GalleryPreviewTheme {
        Surface {
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
}

@Composable
@PreviewDayNight
internal fun GalleryBottomSheetPreview() {
    GalleryPreviewTheme {
        GallerySheetContent(eventSink = { })
    }
}

@Composable
private fun GalleryPreviewTheme(content: @Composable () -> Unit) {
    MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        content()
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
