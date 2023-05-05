package io.ashdavies.cloud

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.util.KtorDsl

private val DefaultHttpConfig: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
    install(ContentNegotiation, ContentNegotiation.Config::json)
}

@KtorDsl
internal fun testMainApplication(
    configuration: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = DefaultHttpConfig,
    application: Application.() -> Unit = { main() },
    block: suspend ApplicationTestBuilder.(HttpClient) -> Unit,
) = testApplication {
    val client = createClient(configuration)
    application(application)
    block(client)
}
