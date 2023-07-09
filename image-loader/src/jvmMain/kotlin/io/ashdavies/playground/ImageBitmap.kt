package io.ashdavies.playground

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

internal actual fun ImageBitmap(bytes: ByteArray): ImageBitmap = Image
    .makeFromEncoded(bytes)
    .toComposeImageBitmap()
