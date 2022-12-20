package io.ashdavies.cloud

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

private object SystemOutLogger : Logger {
    override fun log(message: String) = println(message)
}

public fun TestHttpClient(url: String, block: HttpClientConfig<*>.() -> Unit = { }): HttpClient {
    return TestHttpClient {
        defaultRequest { url(url) }
        block()
    }
}

public fun TestHttpClient(block: HttpClientConfig<*>.() -> Unit = { }): HttpClient {
    return HttpClient {
        install(ContentNegotiation) { json() }

        install(Logging) {
            logger = SystemOutLogger
            level = LogLevel.HEADERS
        }

        block()
    }
}
