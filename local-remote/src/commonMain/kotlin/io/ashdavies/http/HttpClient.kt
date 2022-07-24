package io.ashdavies.http

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

private const val DEFAULT_FUNCTIONS_HOST = "https://europe-west1-playground-1a136.cloudfunctions.net/"
private const val LOCAL_FUNCTIONS_HOST = "http://localhost:5001/"

private val DefaultUserAgent: String
    get() = "${Software.clientName} (${Software.productName}; ${Software.buildVersion})"

private fun DefaultHttpClient(block: HttpClientConfig<*>.() -> Unit = { }): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            serializersModule = SerializersModule { contextual(EventsSerializer) }
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
        url(DEFAULT_FUNCTIONS_HOST)
        userAgent(DefaultUserAgent)
    }

    install(HttpTimeout) {
        connectTimeoutMillis = INFINITE_TIMEOUT_MS
    }

    install(Logging) {
        level = LogLevel.HEADERS
    }

    block()
}

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    DefaultHttpClient { /*install(HttpCache)*/ }
}

@Composable
public inline fun <reified T : Any> requestingState(
    client: HttpClient = LocalHttpClient.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    noinline block: HttpRequestBuilder.() -> Unit = { }
): State<Result<T>> = client
    .requestingInternal<T>(scope, block)
    .collectAsState()

public suspend inline fun <reified T : Any> HttpClient.requesting(
    urlString: String, noinline block: HttpRequestBuilder.() -> Unit = { }
): StateFlow<Result<T>> = requesting {
    url.takeFrom(urlString)
    block()
}

public suspend inline fun <reified T : Any> HttpClient.requesting(
    noinline block: HttpRequestBuilder.() -> Unit = { }
): StateFlow<Result<T>> = coroutineScope {
    requestingInternal(this, block)
}

@PublishedApi
internal inline fun <reified T : Any> HttpClient.requestingInternal(
    scope: CoroutineScope, noinline block: HttpRequestBuilder.() -> Unit = { },
): StateFlow<Result<T>> = flow<Result<T>> {
    emit(Result.success(request {
        onProgress { emit(Result.loading(it)) }
        block()
    }.body()))
}.stateIn(scope, SharingStarted.Lazily, Result.loading())

public fun HttpClient.defaultRequest(
    configure: DefaultRequest.DefaultRequestBuilder.() -> Unit,
): HttpClient = config {
    install(DefaultRequest, configure)
}

public fun HttpClient.header(
    key: String,
    value: Any?,
): HttpClient = defaultRequest {
    header(key, value)
}

public fun HttpClient.url(
    block: URLBuilder.() -> Unit
): HttpClient = defaultRequest {
    url(block)
}
