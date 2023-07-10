package io.ashdavies.gallery

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

public actual typealias Uri = java.net.URI

@Composable
public actual fun CameraView(
    modifier: Modifier,
    onCapture: (Uri) -> Unit
) {
    Text("Unsupported")
}

