package dev.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.ashdavies.tally.files.Path
import dev.ashdavies.tally.material.OperationNotImplemented

@Composable
internal actual fun ImageCapture(
    onResult: (Path?) -> Unit,
    modifier: Modifier,
) {
    OperationNotImplemented()
}
