package dev.ashdavies.playground.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.ashdavies.playground.files.Path
import java.io.FilenameFilter
import javax.imageio.ImageIO

@Composable
internal actual fun ImageCapture(
    onResult: (Path?) -> Unit,
    modifier: Modifier,
) {
    val fileSuffixes = ImageIO.getReaderFileSuffixes()
    val filenameFilter = FilenameFilter { _, name ->
        fileSuffixes.any { name.endsWith(it) }
    }

    FileDialog(
        title = "Select an image",
        onCreate = { it.filenameFilter = filenameFilter },
        onClose = { onResult(it?.let(::Path)) },
    )
}
