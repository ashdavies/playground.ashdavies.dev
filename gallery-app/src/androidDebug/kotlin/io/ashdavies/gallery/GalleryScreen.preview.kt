package io.ashdavies.gallery

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterial3Api::class)
internal fun GalleryTopAppBarPreview() {
    GalleryPreviewTheme {
        GalleryTopAppBar(enterAlwaysScrollBehavior()) { }
    }
}

@Composable
@Preview(heightDp = 120)
@OptIn(ExperimentalFoundationApi::class)
@Preview(heightDp = 120, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun GalleryGridPreview() {
    GalleryPreviewTheme {
        Surface {
            GalleryGrid(
                itemList = persistentListOf(
                    GalleryScreenStateItem(),
                    GalleryScreenStateItem(isSelected = true),
                    GalleryScreenStateItem(state = SyncState.SYNCING),
                    GalleryScreenStateItem(state = SyncState.SYNCED),
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

private fun GalleryScreenStateItem(
    name: String = "Sample Image",
    isSelected: Boolean = false,
    state: SyncState = SyncState.NOT_SYNCED,
) = GalleryScreen.State.StandardItem(
    title = name,
    imageModel = "https://picsum.photos/200",
    isSelected = isSelected,
    state = state,
)
