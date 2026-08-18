package dev.ashdavies.cloud

import dev.ashdavies.http.common.models.AuthResult
import dev.ashdavies.http.common.models.XApiKey
import dev.zacsweers.metro.createGraph
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

private val DefaultHttpConfig: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
    install(ContentNegotiation, ContentNegotiationConfig::json)
}

@ExperimentalCoroutinesApi
internal class ApplicationTest {

    @Test
    fun `should sign in with custom token`() = testMainApplication { client ->
        val httpResponse = client.post("/firebase/auth") {
            header(HttpHeaders.XApiKey, assertNotNull(JvmIntegrationTestBuildConfig.API_KEY, "API_KEY was null"))
            setBody(mapOf("uid" to "jane.smith@example.com"))
            contentType(ContentType.Application.Json)
        }

        if (httpResponse.status != HttpStatusCode.OK) {
            fail(httpResponse.bodyAsText())
        }

        assertNotNull(httpResponse.body<AuthResult>().idToken)
    }

    /**
     * TODO Create test to verify expired token renewal
     */

    @Test
    @Ignore
    fun `should aggregate github events`() = testMainApplication { client ->
        val response = client.post("/events:aggregate") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}

private fun testMainApplication(block: suspend ApplicationTestBuilder.(HttpClient) -> Unit) = testApplication {
    val client = createClient(DefaultHttpConfig)
    val graph = createGraph<CloudRunGraph>()
    application { main(graph.routes) }
    block(client)
}
