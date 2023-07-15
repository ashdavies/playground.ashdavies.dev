package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun CameraOverlay(
    paths: FileProvider,
    modifier: Modifier = Modifier,
    onCapture: (Uri) -> Unit,
)
