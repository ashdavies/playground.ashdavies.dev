package io.ashdavies.party.gallery

import app.cash.paparazzi.Paparazzi
import io.ashdavies.party.tooling.MaterialPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import kotlin.test.Test

internal class GalleryScreenTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun galleryGrid() {
        paparazzi.snapshot {
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
    }

    @Test
    fun gallerySheetContent() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                GallerySheetContent(eventSink = { })
            }
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
