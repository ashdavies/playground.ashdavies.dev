package io.ashdavies.playground.network

import io.ashdavies.playground.ComposableCompositionLocal
import io.ashdavies.playground.EventsSerializer
import io.ashdavies.playground.composableCompositionLocalOf
import io.ashdavies.playground.profile.RandomUser
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

private const val DEFAULT_HOST = "https://europe-west1-playground-1a136.cloudfunctions.net/"
private const val DEFAULT_USER_AGENT = "Ktor/2.0.0-beta-1 (Android; S3B1.220218.006)"

public val LocalHttpClient: ComposableCompositionLocal<HttpClient> = composableCompositionLocalOf {
    HttpClient {
        install(ContentNegotiation) {
            json(Json {
                serializersModule = SerializersModule {
                    contextual(Envelope.serializer(RandomUser.serializer()))
                    contextual(ListSerializer(EventsSerializer))
                    contextual(EventsSerializer)
                }

                ignoreUnknownKeys = true
            })
        }

        install(DefaultRequest) {
            url(DEFAULT_HOST) {
                header(HttpHeaders.UserAgent, DEFAULT_USER_AGENT)
                protocol = URLProtocol.HTTPS
                takeFrom(DEFAULT_HOST)
            }
        }

        install(HttpCache)
        install(Logging)
    }
}
