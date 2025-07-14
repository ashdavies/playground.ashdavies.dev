package io.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ashdavies.tally.material.OperationNotImplemented
import kotlinx.io.files.Path

@Composable
internal actual fun ImageCapture(
    target: Path,
    onResult: (Boolean) -> Unit,
    modifier: Modifier,
) {
    OperationNotImplemented()
}
