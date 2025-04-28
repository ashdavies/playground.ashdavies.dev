package io.ashdavies.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public fun defaultHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient {
    DefaultHttpConfiguration()
    block()
}

public val DefaultHttpConfiguration: HttpClientConfig<*>.() -> Unit = {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }

    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
}
