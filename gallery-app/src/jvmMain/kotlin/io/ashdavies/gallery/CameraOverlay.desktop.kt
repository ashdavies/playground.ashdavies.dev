package io.ashdavies.gallery

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun CameraOverlay(
    manager: StorageManager,
    modifier: Modifier,
    onCapture: (File) -> Unit,
) {
    Text("Unsupported")
}
