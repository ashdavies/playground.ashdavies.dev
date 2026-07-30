package dev.ashdavies.cloud

import dev.ashdavies.cloud.google.GoogleApiException
import dev.ashdavies.http.common.models.XApiKey
import dev.ashdavies.http.common.models.XFirebaseAppCheck
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.http.throwClientRequestExceptionAs
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.CompressionConfig
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.CORSConfig
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing

@DependencyGraph(AppScope::class)
internal interface CloudRunGraph {

    val embeddedServer: EmbeddedServer<*, *>
    val routes: Set<CloudRunRoute>

    @Provides
    fun embeddedServer(routes: Set<CloudRunRoute>): EmbeddedServer<*, *> = embeddedServer(
        module = { main(routes) },
        factory = Netty,
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

internal fun Application.main(routes: Set<CloudRunRoute>) {
    install(RequestHeaderDumper)

    install(Authentication, AuthenticationConfig::appCheck)

    install(CallLogging)
    install(Compression, CompressionConfig::default)
    install(ContentNegotiation, Configuration::json)
    install(ConditionalHeaders)
    install(CORS, CORSConfig::install)

    install(DefaultHeaders) {
        header(HttpHeaders.Server, System.getProperty("os.name"))
    }

    routing(routes)
}

private fun CORSConfig.install() {
    allowHost("localhost:5000")
    allowHost("localhost:8081")

    allowHeader(HttpHeaders.Authorization)
    allowHeader(HttpHeaders.ContentType)

    allowHeader(HttpHeaders.XApiKey)
    allowHeader(HttpHeaders.XFirebaseAppCheck)

    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Put)

    allowCredentials = true
    maxAgeInSeconds = 3600
}

private fun Application.routing(routes: Set<CloudRunRoute>) = routing {
    routes.forEach { with(it) { invoke() } }
}

public interface CloudRunRoute {
    public operator fun Routing.invoke(): Route
}

private val RequestHeaderDumper = createApplicationPlugin("RequestHeaderDumper") {
    onCall { call ->
        // Standard System.out bypasses Logback/SLF4J levels completely
        println("==================================================")
        println("--> INCOMING REQUEST: ${call.request.httpMethod.value} ${call.request.uri}")
        println("------------------- HEADERS -------------------")
        call.request.headers.forEach { name, values ->
            println("$name: ${values.joinToString(", ")}")
        }
        println("==================================================\n")
    }
}
