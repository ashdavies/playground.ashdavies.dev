package io.ashdavies.cloud

import io.ashdavies.content.PlatformContext
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.http.publicStorage
import io.ashdavies.http.throwClientRequestExceptionAs
import io.ashdavies.io.resolveCacheDir
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.http.HttpHeaders
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.CompressionConfig
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.defaultheaders.DefaultHeadersConfig
import io.ktor.server.routing.routing

public fun main() {
    val server = embeddedServer(
        module = Application::main,
        factory = CIO,
        port = 8080,
    )

    server.start(wait = true)
}

internal fun Application.main() {
    val client = HttpClient {
        DefaultHttpConfiguration()

        install(HttpCache) {
            publicStorage(PlatformContext.Default.resolveCacheDir())
        }

        install(HttpCallValidator) {
            throwClientRequestExceptionAs<GoogleApiException>()
        }

        expectSuccess = true
    }

    install(DefaultHeaders, DefaultHeadersConfig::headers)
    install(Compression, CompressionConfig::default)
    install(ContentNegotiation, Configuration::json)
    install(ConditionalHeaders)
    install(CallLogging)

    routing {
        events(client)
        firebase(client)
        hello()
        static()
    }
}

private fun DefaultHeadersConfig.headers() {
    header(HttpHeaders.Server, System.getProperty("os.name"))
}
