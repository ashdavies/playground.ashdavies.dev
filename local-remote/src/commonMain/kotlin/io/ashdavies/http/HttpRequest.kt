package io.ashdavies.http

import io.ktor.client.plugins.onDownload
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.http.path
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap

@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> HttpRequestBuilder.parameter(value: T) {
    Properties.encodeToMap(value)
        .filter { (key, _) -> key != "type" && key.startsWith("value") }
        .forEach { parameter(it.key.substringAfter("value."), it.value) }
}

public fun HttpRequestBuilder.path(vararg path: String) {
    url.path(*path)
}

@PublishedApi
internal fun HttpRequestBuilder.onProgress(listener: suspend (Float) -> Unit) {
    onDownload { bytesSentTotal, contentLength ->
        listener((bytesSentTotal.toFloat() / contentLength).coerceAtMost(1.0F))
    }
}
