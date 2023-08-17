package io.ashdavies.http

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.ashdavies.playground.EventsSerializer
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeout.Plugin.INFINITE_TIMEOUT_MS
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
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
        level = LogLevel.INFO
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
