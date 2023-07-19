package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun CameraOverlay(
    manager: StorageManager,
    modifier: Modifier = Modifier,
    onCapture: (File) -> Unit,
)
