package io.ashdavies.http

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.staticCompositionLocalOf
import io.ashdavies.playground.EventsSerializer
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeout.Plugin.INFINITE_TIMEOUT_MS
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.URLBuilder
import io.ktor.http.path
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlin.reflect.KProperty

private const val DEFAULT_FUNCTIONS_HOST = "https://europe-west1-playground-1a136.cloudfunctions.net/"
private const val LOCAL_FUNCTIONS_HOST = "http://localhost:5001/"

private val DefaultUserAgent: String
    get() = "Ktor/2.0.0 (${softwareVersion.productName}; ${softwareVersion.buildVersion})"

private fun DefaultHttpClient(block: HttpClientConfig<*>.() -> Unit = { }): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            serializersModule = SerializersModule { contextual(EventsSerializer) }
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    install(DefaultRequest) {
        url(DEFAULT_FUNCTIONS_HOST)
        userAgent(DefaultUserAgent)
    }

    install(HttpTimeout) {
        connectTimeoutMillis = INFINITE_TIMEOUT_MS
    }

    install(Logging) {
        level = LogLevel.ALL
    }

    block()
}

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    DefaultHttpClient { /*install(HttpCache)*/ }
}

public fun HttpClient.defaultRequest(configure: DefaultRequest.DefaultRequestBuilder.() -> Unit): HttpClient {
    return config { install(DefaultRequest, configure) }
}

public fun HttpClient.header(key: String, value: Any?): HttpClient = defaultRequest { header(key, value) }

public fun HttpClient.url(block: URLBuilder.() -> Unit): HttpClient = defaultRequest { url(block) }

@Composable
public inline fun <reified T : Any> requesting(
    client: HttpClient = LocalHttpClient.current,
    noinline block: HttpRequestBuilder.() -> Unit = { },
): State<Result<T>> = produceState {
    value = Result.success(client.request {
        onProgress { value = Result.loading(it) }
        block()
    }.body())
}

public operator fun <T> State<Result<T>>.getValue(thisRef: Any?, property: KProperty<*>): T {
    while (value.isLoading) { /* ... */ }
    return value.getOrThrow()
}

@PublishedApi
internal fun HttpRequestBuilder.onProgress(listener: suspend (Float) -> Unit) {
    onDownload { bytesSentTotal, contentLength ->
        listener((bytesSentTotal.toFloat() / contentLength).coerceAtMost(1.0F))
    }
}

public fun HttpRequestBuilder.path(vararg path: String) {
    url.path(*path)
}
