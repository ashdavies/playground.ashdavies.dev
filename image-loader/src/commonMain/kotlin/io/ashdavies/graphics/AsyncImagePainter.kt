package io.ashdavies.graphics

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
public expect fun rememberAsyncImagePainter(
    model: Any?,
    contentScale: ContentScale = ContentScale.Fit,
): Painter
