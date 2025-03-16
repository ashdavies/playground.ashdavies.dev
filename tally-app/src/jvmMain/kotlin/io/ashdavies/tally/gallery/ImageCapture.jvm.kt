package io.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.io.FilenameFilter
import javax.imageio.ImageIO

@Composable
internal actual fun ImageCapture(
    manager: StorageManager,
    modifier: Modifier,
    onCapture: (File?) -> Unit,
) {
    val fileSuffixes = ImageIO.getReaderFileSuffixes()
    val filenameFilter = FilenameFilter { _, name ->
        fileSuffixes.any { name.endsWith(it) }
    }

    FileDialog(
        title = "Select an image",
        onCreate = { it.filenameFilter = filenameFilter },
        onClose = { directory, file -> onCapture(File(directory, file)) },
    )
}
