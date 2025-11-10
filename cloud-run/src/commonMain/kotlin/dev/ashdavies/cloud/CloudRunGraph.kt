package dev.ashdavies.cloud

import dev.ashdavies.cloud.google.GoogleApiException
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.http.throwClientRequestExceptionAs
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.http.HttpHeaders
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.CompressionConfig
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing

@DependencyGraph(AppScope::class)
internal interface CloudRunGraph {

    val embeddedServer: EmbeddedServer<*, *>

    @Provides
    fun embeddedServer(routes: Set<CloudRunRoute>): EmbeddedServer<*, *> = embeddedServer(
        module = {
            install(DefaultHeaders) {
                header(HttpHeaders.Server, System.getProperty("os.name"))
            }

            install(Compression, CompressionConfig::default)
            install(ContentNegotiation, Configuration::json)
            install(ConditionalHeaders)
            install(CallLogging)

            routing {
                routes.forEach {
                    with(it) { invoke() }
                }
            }
        },
        factory = CIO,
        port = 8080,
    )

    @Provides
    fun httpClient(): HttpClient = defaultHttpClient {
        install(HttpCallValidator) {
            throwClientRequestExceptionAs<GoogleApiException>()
        }

        expectSuccess = true
    }
}

public interface CloudRunRoute {
    public operator fun Routing.invoke(): Route
}
