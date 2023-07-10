package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public expect fun CameraView(
    modifier: Modifier = Modifier,
    onCapture: (Uri) -> Unit,
)

public expect class Uri
