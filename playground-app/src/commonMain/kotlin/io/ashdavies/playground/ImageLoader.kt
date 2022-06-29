package io.ashdavies.playground

import androidx.compose.foundation.Image
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
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
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

public object EmptyPainter : Painter() {
    override val intrinsicSize: Size get() = Size.Unspecified
    override fun DrawScope.onDraw(): Unit = Unit
}

internal expect fun ImageBitmap(bytes: ByteArray): ImageBitmap

@Composable
public fun produceImagePainterState(urlString: String? = null): State<Result<Painter>> = when (urlString) {
    null -> mutableStateOf(Result.success<Painter>(EmptyPainter))
    else -> produceImagePainterState(
        client = LocalHttpClient.current,
        urlString = urlString
    )
}

@Composable
internal fun produceImagePainterState(client: HttpClient, urlString: String): State<Result<Painter>> = produceState {
    val response = client.get(urlString) { onProgress { value = Result.loading(it) } }
    value = Result.success(BitmapPainter(ImageBitmap(response.body())))
}

@Composable
public fun RemoteImage(urlString: String, modifier: Modifier = Modifier, contentDescription: String? = null) {
    val resource: Result<Painter> by produceImagePainterState(urlString)

    resource.fold(
        onSuccess = { Image(it, contentDescription, modifier, contentScale = FillWidth) },
        onLoading = { Box { CircularProgressIndicator(it, modifier.padding(48.dp)) } },
        onFailure = {
            Image(
                painter = rememberVectorPainter(Icons.Filled.Close),
                modifier = modifier.padding(48.dp),
                contentDescription = it.message,
            )
        },
    )
}

private fun HttpRequestBuilder.onProgress(listener: suspend (Float) -> Unit) {
    onDownload { bytesSentTotal, contentLength ->
        listener((bytesSentTotal.toFloat() / contentLength).coerceAtMost(1.0F))
    }
}
