package io.ashdavies.http

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.ashdavies.playground.EventsSerializer
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeout.Plugin.INFINITE_TIMEOUT_MS
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.takeFrom
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

private const val DEFAULT_FUNCTIONS_HOST = "https://europe-west1-playground-1a136.cloudfunctions.net/"
private const val DEFAULT_USER_AGENT = "Ktor/2.0.0 (Android; S3B1.220218.006)"

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    HttpClient {
        install(ContentNegotiation) {
            json(Json {
                serializersModule = SerializersModule { contextual(EventsSerializer) }
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }

        install(DefaultRequest) {
            url {
                takeFrom(DEFAULT_FUNCTIONS_HOST)
                userAgent(DEFAULT_USER_AGENT)
            }
        }

        // install(HttpCache)

        install(HttpTimeout) {
            connectTimeoutMillis = INFINITE_TIMEOUT_MS
        }

        install(Logging) {
            level = LogLevel.ALL
        }
    }
}

@Composable
public fun ProvideHttpClient(
    client: HttpClient = LocalHttpClient.current,
    configure: HttpClientConfig<*>.() -> Unit = { },
    content: @Composable () -> Unit,
) {
    val copy = HttpClient {
        install(client)
        configure()
    }

    CompositionLocalProvider(
        LocalHttpClient provides copy,
        content = content
    )
}
