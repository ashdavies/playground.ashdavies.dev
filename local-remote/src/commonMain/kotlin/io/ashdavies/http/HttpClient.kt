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
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.header
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
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.contextual

private val defaultUserAgent: String
    get() = "${Software.clientName} (${Software.productName}; ${Software.buildVersion})"

private fun DefaultLogger(block: (message: String) -> Unit = ::println) = object : Logger {
    override fun log(message: String) = block(message)
}

public fun DefaultHttpClient(configure: HttpClientConfig<*>.() -> Unit = { }): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(DefaultJson())
    }

    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        userAgent(defaultUserAgent)
    }

    install(HttpTimeout) {
        connectTimeoutMillis = INFINITE_TIMEOUT_MS
    }

    install(Logging) {
        logger = DefaultLogger()
        level = LogLevel.ALL
    }

    configure()
}

private fun DefaultJson(serializers: SerializersModuleBuilder.() -> Unit = { }) = Json {
    serializersModule = SerializersModule {
        contextual(EventsSerializer)
        serializers()
    }

    ignoreUnknownKeys = true
    encodeDefaults = true
}

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    DefaultHttpClient { install(HttpCache) }
}

@Composable
public inline fun <reified T : Any> requestingState(
    client: HttpClient = LocalHttpClient.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    noinline block: HttpRequestBuilder.() -> Unit = { },
): State<Result<T>> = client
    .requestingInternal<T>(scope, block)
    .collectAsState()

public suspend inline fun <reified T : Any> HttpClient.requesting(
    urlString: String,
    noinline block: HttpRequestBuilder.() -> Unit = { },
): StateFlow<Result<T>> = requesting {
    url.takeFrom(urlString)
    block()
}

public suspend inline fun <reified T : Any> HttpClient.requesting(
    noinline block: HttpRequestBuilder.() -> Unit = { },
): StateFlow<Result<T>> = coroutineScope {
    requestingInternal(this, block)
}

@PublishedApi
internal inline fun <reified T : Any> HttpClient.requestingInternal(
    scope: CoroutineScope,
    noinline block: HttpRequestBuilder.() -> Unit = { },
): StateFlow<Result<T>> = flow<Result<T>> {
    val response = request {
        onProgress { emit(Result.loading(it)) }
        block()
    }.body<T>()

    emit(Result.success(response))
}.stateIn(scope, SharingStarted.Lazily, Result.loading())

private fun HttpClient.defaultRequest(
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
    block: URLBuilder.() -> Unit,
): HttpClient = defaultRequest {
    url(block)
}
