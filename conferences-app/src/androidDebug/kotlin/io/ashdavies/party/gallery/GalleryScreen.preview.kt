package io.ashdavies.party.gallery

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf

@Composable
@Preview(heightDp = 120)
@OptIn(ExperimentalFoundationApi::class)
@Preview(heightDp = 120, uiMode = Configuration.UI_MODE_NIGHT_YES)
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
                isSelecting = true,
                onSelect = { },
                onExpand = { },
            )
        }
    }
}

@Preview
@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
