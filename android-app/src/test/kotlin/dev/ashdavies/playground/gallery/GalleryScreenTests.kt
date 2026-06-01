package dev.ashdavies.playground.gallery

import androidx.compose.ui.Modifier
import app.cash.paparazzi.Paparazzi
import dev.ashdavies.playground.tooling.MaterialPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

internal class GalleryScreenTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                GalleryUi(
                    state = GalleryScreenState(
                        itemList = persistentListOf(
                            galleryScreenStateItem(),
                            galleryScreenStateItem(isSelected = true),
                            galleryScreenStateItem(state = SyncState.SYNCING),
                            galleryScreenStateItem(state = SyncState.SYNCED),
                        ),
                        expandedItem = null,
                        showCapture = false,
                        eventSink = { },
                    ),
                    modifier = Modifier,
                )
            }
        }
    }
}

private fun galleryScreenStateItem(
    name: String = "Sample Image",
    isSelected: Boolean = false,
    state: SyncState = SyncState.NOT_SYNCED,
) = GalleryScreenState.StandardItem(
    title = name,
    imageModel = null,
    isSelected = isSelected,
    state = state,
)
