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
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    DefaultHttpClient { install(HttpCache) }
}

public fun DefaultHttpClient(
    credentials: HttpCredentials,
    configure: HttpClientConfig<*>.() -> Unit = { },
): HttpClient = DefaultHttpClient {
    install(DefaultRequest) { userAgent(credentials.userAgent) }
    configure()
}

public fun DefaultHttpClient(
    engine: HttpClientEngine = CIO.create { },
    block: HttpClientConfig<*>.() -> Unit = { },
): HttpClient = HttpClient(engine) {
    install(ContentNegotiation) {
        json(
            json = Json {
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
        logger = DefaultLogger()
        level = LogLevel.INFO
    }

    block()
}

private fun DefaultLogger(block: (message: String) -> Unit = ::println) = object : Logger {
    override fun log(message: String) = block(message)
}
