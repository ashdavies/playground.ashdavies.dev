package io.ashdavies.gallery

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun CameraOverlay(
    paths: FileProvider,
    modifier: Modifier,
    onCapture: (Uri) -> Unit,
) {
    Text("Unsupported")
}
