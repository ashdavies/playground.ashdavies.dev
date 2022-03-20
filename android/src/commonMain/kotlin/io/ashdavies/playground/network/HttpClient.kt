package io.ashdavies.playground.network

import io.ashdavies.playground.ComposableCompositionLocal
import io.ashdavies.playground.EventsSerializer
import io.ashdavies.playground.composableCompositionLocalOf
import io.ashdavies.playground.profile.RandomUser
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

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

        install(HttpCache)
        install(Logging)
    }
}
