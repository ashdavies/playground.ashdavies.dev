package io.ashdavies.playground.network

import androidx.compose.runtime.staticCompositionLocalOf
import io.ashdavies.playground.compose.staticComposableCompositionLocalOf
import io.ashdavies.playground.database.EventsSerializer
import io.ashdavies.playground.profile.RandomUser
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val LocalHttpClient = staticComposableCompositionLocalOf {
    val json: Json = LocalJson.current

    HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}

val LocalJson = staticCompositionLocalOf {
    Json {
        serializersModule = SerializersModule {
            contextual(Envelope.serializer(RandomUser.serializer()))
            contextual(ListSerializer(EventsSerializer))
            contextual(EventsSerializer)
        }

        ignoreUnknownKeys = true
    }
}
