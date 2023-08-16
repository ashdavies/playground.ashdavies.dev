package io.ashdavies.graphics

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
public actual fun rememberAsyncImagePainter(
    model: Any?,
    contentScale: ContentScale,
): Painter = rememberAsyncImagePainter(
    model = model,
    contentScale = contentScale,
)
