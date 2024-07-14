package io.ashdavies.cloud

import io.ashdavies.http.DefaultHttpClient
import io.ashdavies.http.HttpClientProvider
import io.ktor.client.HttpClient
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

@OptIn(HttpClientProvider::class)
internal fun Application.main(client: HttpClient = DefaultHttpClient()) {
    install(DefaultHeaders, DefaultHeadersConfig::headers)
    install(Compression, CompressionConfig::default)
    install(ContentNegotiation, Configuration::json)
    install(ConditionalHeaders)
    install(CallLogging)

    routing {
        events()
        firebase(client)
        hello()
        static()
    }
}

private fun DefaultHeadersConfig.headers() {
    header(HttpHeaders.Server, System.getProperty("os.name"))
}
