package io.ashdavies.http

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    defaultHttpClient()
}

public fun defaultHttpClient(
    engine: HttpClientEngine = OkHttp.create { },
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

    install(HttpCache) {
        val cacheFile = Files
            .createDirectories(Paths.get("build/cache"))
            .toFile()

        publicStorage(FileStorage(cacheFile))
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
