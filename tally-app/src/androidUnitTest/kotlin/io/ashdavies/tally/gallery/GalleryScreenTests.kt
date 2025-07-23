package io.ashdavies.tally.gallery

import androidx.compose.ui.Modifier
import app.cash.paparazzi.Paparazzi
import io.ashdavies.tally.tooling.MaterialPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import kotlin.test.Test

internal class GalleryScreenTests {

    private val galleryUi = GalleryUi()

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                galleryUi.Content(
                    state = GalleryScreen.State(
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
) = GalleryScreen.State.StandardItem(
    title = name,
    imageModel = null,
    isSelected = isSelected,
    state = state,
)
