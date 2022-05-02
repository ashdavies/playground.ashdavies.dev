package io.ashdavies.playground

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

internal actual fun ImageBitmap(bytes: ByteArray): ImageBitmap = BitmapFactory
    .decodeByteArray(bytes, 0, bytes.size)
    .asImageBitmap()

