package io.ashdavies.http

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    defaultHttpClient { install(HttpCache) }
}

public fun defaultHttpClient(
    engine: HttpClientEngine = CIO.create { },
    block: HttpClientConfig<*>.() -> Unit = { },
): HttpClient = HttpClient(engine) {
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
    }

    install(Logging) {
        logger = Logger()
        level = LogLevel.ALL
    }

    block()
}

private fun Logger(block: (message: String) -> Unit = ::println): Logger = object : Logger {
    override fun log(message: String) = block(message)
}
