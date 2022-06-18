package io.ashdavies.playground

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map

public val Resource<*>.isLoading: Boolean
    get() = this is Resource.Loading

public object EmptyPainter : Painter() {
    override val intrinsicSize: Size get() = Size.Unspecified
    override fun DrawScope.onDraw(): Unit = Unit
}

internal expect fun ImageBitmap(bytes: ByteArray): ImageBitmap

internal class ImageLoader(private val client: HttpClient) : (Url) -> Flow<Resource<ByteReadChannel>> {
    override fun invoke(url: Url): Flow<Resource<ByteReadChannel>> = channelFlow {
        val response: HttpResponse = client.get(url) {
            onDownload { bytesSentTotal, contentLength ->
                val progress = (bytesSentTotal.toFloat() / contentLength).coerceAtMost(1.0F)
                send(Resource.Loading(progress))
            }
        }

        send(Resource.Success(response.body()))
    }
}

@Composable
public fun imagePainter(urlString: String): Resource<Painter> = imagePainter(HttpClient(), Url(urlString))

@Composable
public fun imagePainter(client: HttpClient = LocalHttpClient.current, url: Url): Resource<Painter> {
    var painter: Resource<Painter> by remember(url) { mutableStateOf(Resource.Loading(0F)) }
    val loader = remember { ImageLoader(client) }

    val flow = loader(url).map { resource -> resource.map { BitmapPainter(ImageBitmap(it.toByteArray())) } }
    LaunchedEffect(url) { flow.collect { painter = it } }
    return painter
}

public sealed class Resource<out T : Any> {
    public data class Loading(val progress: Float) : Resource<Nothing>()
    public data class Success<out T : Any>(val value: T) : Resource<T>()
    public data class Failure(val exception: Throwable) : Resource<Nothing>()
}

/**
 * TODO Create event listener for image loading logging
 */
@Composable
public fun RemoteImage(urlString: String, modifier: Modifier = Modifier, contentDescription: String? = null) {
    when (val resource: Resource<Painter> = imagePainter(urlString)) {
        is Resource.Loading -> Box { CircularProgressIndicator(resource.progress, modifier.padding(48.dp)) }
        is Resource.Success -> Image(resource.value, contentDescription, modifier, contentScale = FillWidth)
        is Resource.Failure -> Image(
            painter = rememberVectorPainter(Icons.Filled.Close),
            contentDescription = resource.exception.message,
            modifier = modifier.padding(48.dp),
        )
    }
}

public inline fun Resource<Painter>.getOrElse(
    onFailure: (Throwable) -> Painter = { EmptyPainter },
    onLoading: (Float) -> Painter = { EmptyPainter },
): Painter = fold(
    onFailure = onFailure,
    onLoading = onLoading,
    onSuccess = { it },
)

@PublishedApi
internal inline fun <T : Any, R : Any> Resource<T>.fold(
    onFailure: (Throwable) -> R,
    onLoading: (Float) -> R,
    onSuccess: (T) -> R
): R = when (this) {
    is Resource.Failure -> onFailure(exception)
    is Resource.Loading -> onLoading(progress)
    is Resource.Success -> onSuccess(value)
}

@PublishedApi
internal inline fun <T : Any, R : Any> Resource<T>.map(
    transform: (T) -> R
): Resource<R> = when (this) {
    is Resource.Loading -> Resource.Loading(progress)
    is Resource.Failure -> Resource.Failure(exception)
    is Resource.Success -> try {
        Resource.Success(transform(value))
    } catch (exception: Exception) {
        Resource.Failure(exception)
    }
}
