package io.ashdavies.http

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    DefaultHttpClient { install(HttpCache) }
}

private val defaultUserAgent: String
    get() = "${Software.clientName} (${Software.productName}; ${Software.buildVersion})"

public fun DefaultHttpClient(
    logLevel: LogLevel = LogLevel.INFO,
    configure: HttpClientConfig<*>.() -> Unit = { },
): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            },
        )
    }

    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        userAgent(defaultUserAgent)
    }

    install(Logging) {
        logger = DefaultLogger()
        level = logLevel
    }

    configure()
}

private fun DefaultLogger(block: (message: String) -> Unit = ::println) = object : Logger {
    override fun log(message: String) = block(message)
}
