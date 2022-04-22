package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

private const val DEFAULT_HOST = "https://europe-west1-playground-1a136.cloudfunctions.net/"
private const val DEFAULT_USER_AGENT = "Ktor/2.0.0 (Android; S3B1.220218.006)"

public val LocalHttpClient: ProvidableCompositionLocal<HttpClient> = staticCompositionLocalOf {
    HttpClient {
        install(ContentNegotiation) {
            json(Json {
                serializersModule = SerializersModule {
                    // contextual(Envelope.serializer(RandomUser.serializer()))
                    contextual(ListSerializer(EventsSerializer))
                    contextual(EventsSerializer)
                }

                ignoreUnknownKeys = true
            })
        }

        install(DefaultRequest) {
            url {
                header(HttpHeaders.UserAgent, DEFAULT_USER_AGENT)
                protocol = URLProtocol.HTTPS
                // takeFrom(DEFAULT_HOST)
            }
        }

        install(HttpCache)
        install(Logging)
    }
}

@Composable
public fun ProvideHttpClient(
    client: HttpClient = LocalHttpClient.current,
    block: HttpClientConfig<*>.() -> Unit = { },
    content: @Composable () -> Unit,
) {
    val copy = HttpClient {
        install(client)
        block()
    }

    CompositionLocalProvider(
        LocalHttpClient provides copy,
        content = content
    )
}

@Serializable
public data class Envelope<T>(val results: List<T>)
