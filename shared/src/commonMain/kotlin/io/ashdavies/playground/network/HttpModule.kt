package io.ashdavies.playground.network

import io.ashdavies.playground.Graph
import io.ashdavies.playground.database.ConferenceSerializer
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf

@OptIn(ExperimentalSerializationApi::class)
val Graph<*>.httpClient: HttpClient
    get() = HttpClient {
        val json = Json {
            serializersModule = serializersModuleOf(
                serializer = ListSerializer(ConferenceSerializer)
            )
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
