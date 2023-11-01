package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ashdavies.content.PlatformContext

private val DefaultState = GalleryScreen.State(
    itemList = emptyList(),
    showCapture = false,
    isLoggedIn = false,
    eventSink = { },
)

private val EmptyStorageManager = object : StorageManager {
    override fun create(context: PlatformContext): File = TempFile
    override fun delete(file: File): Boolean = false
}

private val TempFile = File.createTempFile(
    /* prefix = */ "tmp",
    /* suffix = */ null,
)

@Preview
@Composable
internal fun GalleryCaptureDefaultPreview() {
    GalleryCapture(EmptyStorageManager) { }
}

@Preview
@Composable
internal fun GalleryBottomBarDefaultPreview() {
    GalleryBottomBar(DefaultState, isSelecting = false)
}

@Preview
@Composable
internal fun GalleryBottomBarSelectingPreview() {
    GalleryBottomBar(DefaultState, isSelecting = true)
}
