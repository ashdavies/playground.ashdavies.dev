package io.ashdavies.graphics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale.Companion.FillWidth
import androidx.compose.ui.unit.dp
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.fold
import io.ashdavies.http.loading
import io.ashdavies.http.produceState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import androidx.compose.foundation.Image as ComposeImage
import org.jetbrains.skia.Image as SkiaImage

@Composable
public fun produceImagePainterState(
    urlString: String? = null
): State<Result<Painter>> = when (urlString) {
    null -> mutableStateOf(Result.success<Painter>(EmptyPainter))
    else -> produceImagePainterState(
        client = LocalHttpClient.current,
        urlString = urlString
    )
}

@Composable
internal fun produceImagePainterState(
    urlString: String,
    client: HttpClient = HttpClient(),
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
) {
    val modelString = model as? String ?: throw IllegalStateException("Unsupported model '$model'")
    val resource by produceImagePainterState(modelString)

    resource.fold(
        onSuccess = { ComposeImage(it, contentDescription, modifier, contentScale = FillWidth) },
        onLoading = { Box { CircularProgressIndicator(it, modifier.padding(48.dp)) } },
        onFailure = {
            ComposeImage(
                painter = rememberVectorPainter(Icons.Filled.Close),
                modifier = modifier.padding(48.dp),
                contentDescription = it.message,
            )
        },
    )
}
