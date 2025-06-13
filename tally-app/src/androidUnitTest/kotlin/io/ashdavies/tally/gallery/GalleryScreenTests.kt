package io.ashdavies.tally.gallery

import app.cash.paparazzi.Paparazzi
import io.ashdavies.tally.tooling.MaterialPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import kotlin.io.path.createTempFile
import kotlin.test.Test

internal class GalleryScreenTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                GalleryScreen(
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
                    manager = tempFileStorageManager(),
                )
            }
        }
    }
}

private fun tempFileStorageManager() = object : StorageManager {
    override suspend fun create(): File = createTempFile().toFile()
    override suspend fun delete(file: File): Boolean = file.delete()
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
