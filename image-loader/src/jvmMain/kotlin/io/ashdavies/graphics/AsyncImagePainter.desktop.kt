package io.ashdavies.graphics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.File
import java.io.InputStream

@Composable
public actual fun rememberAsyncImagePainter(
    model: Any?,
    contentScale: ContentScale,
): Painter = when {
    model is ImageBitmap -> remember(model) { BitmapPainter(model) }

    model is File && model.isFile -> rememberBitmapPainter(model.inputStream())

    model is ImageVector -> rememberVectorPainter(model)

    model is String && model.startsWith("https://") -> {
        val client = LocalHttpClient.current
        val inputStream by produceState<InputStream?>(null) {
            value = client.get(model)
                .bodyAsChannel()
                .toInputStream()
        }

        inputStream
            ?.let { rememberBitmapPainter(it) }
            ?: EmptyPainter
    }

    else -> error("Unsupported model '$model'")
}

@Composable
private fun rememberBitmapPainter(input: InputStream): Painter = remember(input) {
    BitmapPainter(loadImageBitmap(input))
}
