package io.ashdavies.graphics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.fold
import io.ashdavies.http.loading
import io.ashdavies.http.produceState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import java.io.File
import androidx.compose.foundation.Image as ComposeImage
import org.jetbrains.skia.Image as SkiaImage

@Composable
private fun produceAsyncRemoteImagePainter(
    urlString: String,
    client: HttpClient = LocalHttpClient.current,
): State<Result<Painter>> = produceState {
    val response = client.get(urlString) {
        onDownload { bytesSentTotal, contentLength ->
            val progress = bytesSentTotal.toFloat() / contentLength
            value = Result.loading(progress.coerceAtMost(1.0F))
        }
    }

    val bitmap = SkiaImage.makeFromEncoded(response.body())
    val composed = bitmap.toComposeImageBitmap()
    val painter = BitmapPainter(composed)

    value = Result.success(painter)
}

@Composable
public actual fun AsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
) {
    val resource by when {
        model is File && model.isFile -> mutableStateOf(Result.success(BitmapPainter(loadImageBitmap(model.inputStream()))))
        model is String && model.startsWith("https://") -> produceAsyncRemoteImagePainter(model)
        else -> mutableStateOf(Result.failure(IllegalArgumentException("Unsupported model '$model'")))
    }

    resource.fold(
        onSuccess = {
            ComposeImage(
                painter = it,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale,
            )
        },
        onLoading = {
            Box {
                CircularProgressIndicator(
                    progress = it,
                    modifier = modifier.padding(48.dp),
                )
            }
        },
        onFailure = {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = it.message,
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
            )
        },
    )
}
