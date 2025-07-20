package io.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ashdavies.tally.files.Path
import io.ashdavies.tally.material.OperationNotImplemented

@Composable
internal actual fun ImageCapture(
    onResult: (Path?) -> Unit,
    modifier: Modifier,
) {
    OperationNotImplemented()
}
