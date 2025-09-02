package dev.ashdavies.playground.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.ashdavies.playground.files.Path
import dev.ashdavies.playground.material.OperationNotImplemented

@Composable
internal actual fun ImageCapture(
    onResult: (Path?) -> Unit,
    modifier: Modifier,
) {
    OperationNotImplemented()
}
