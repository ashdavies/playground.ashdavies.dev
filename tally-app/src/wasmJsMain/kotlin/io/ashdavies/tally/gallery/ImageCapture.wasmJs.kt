package io.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ashdavies.tally.material.OperationNotImplemented

@Composable
internal actual fun ImageCapture(
    manager: StorageManager,
    modifier: Modifier,
    onCapture: (File?) -> Unit,
) {
    OperationNotImplemented()
}
