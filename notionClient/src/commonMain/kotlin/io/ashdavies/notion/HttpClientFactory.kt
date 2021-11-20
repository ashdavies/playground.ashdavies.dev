package io.ashdavies.notion

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders.Authorization
import kotlinx.serialization.json.Json
import io.ashdavies.notion.LogLevel as NotionLogLevel
import io.ktor.client.features.logging.LogLevel as KtorLogLevel

private const val NotionVersion = "2021-08-16"

private val NotionLogLevel.ktorLogLevel: KtorLogLevel
    get() = enumValueOf(name)

expect object HttpClientFactory {
    val engine: HttpClientEngineFactory<*>
}

private fun HttpClientFactory.create(
    logLevel: NotionLogLevel,
    block: HttpClientConfig<*>.() -> Unit,
) = HttpClient(engine) {
    defaultRequest {
        header("Notion-Version", NotionVersion)
    }

    install(JsonFeature) {
        serializer = KotlinxSerializer(Json { ignoreUnknownKeys = true })
    }

    Logging {
        level = logLevel.ktorLogLevel
        logger = Logger.DEFAULT
    }

    block()
}

internal fun HttpClientFactory.create(
    logLevel: NotionLogLevel,
): HttpClient = create(logLevel) { }

internal fun HttpClientFactory.create(
    token: String,
    logLevel: NotionLogLevel,
): HttpClient = create(logLevel) {
    defaultRequest {
        header(Authorization, "Bearer $token")
    }
}

internal fun HttpClient.config(user: String, pass: String): HttpClient = config {
    install(Auth) {
        basic {
            username = user
            password = pass
        }
    }
}

