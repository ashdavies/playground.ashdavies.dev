package io.ashdavies.cloud

import io.ashdavies.http.AppCheckToken
import io.ashdavies.playground.models.Event
import io.ashdavies.playground.models.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.util.KtorDsl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val DefaultHttpConfig: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
    install(ContentNegotiation, ContentNegotiation.Config::json)
}

@ExperimentalCoroutinesApi
internal class ApplicationTest {

    @Test
    fun `should sign in with custom token`() = testMainApplication { client ->
        val apiKey = requireNotNull(System.getenv("GOOGLE_PROJECT_API_KEY"))

        val authResult = client.post("/firebase/auth") {
            setBody(mapOf("uid" to "jane.smith@example.com"))
            contentType(ContentType.Application.Json)
            header("X-API-Key", apiKey)
        }.body<Map<String, String>>()

        assertNotNull(authResult["idToken"])
    }

    @Test
    fun `should get events with default limit`() = testMainApplication { client ->
        val response = client.get("/events") { contentType(ContentType.Application.Json) }
        val body = response.body<List<Event>>()

        assertEquals(50, body.size)
    }

    @Test
    fun `should aggregate events`() = testMainApplication { client ->
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }
        val response = client.post("/events:aggregate")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should create test application`() = testMainApplication { client ->
        val response = client.get("/hello")

        assertEquals(
            expected = HttpStatusCode.OK,
            actual = response.status,
        )

        assertEquals(
            actual = response.bodyAsText(),
            expected = "Hello, World!",
        )
    }

    @Test
    fun `should return app check token for request`() = testMainApplication { client ->
        val tokenResponse = client.post("/firebase/token") {
            setBody(FirebaseApp(System.getenv("MOBILE_SDK_APP_ID")))
            // headers { append("X-API-Key", playgroundApiKey) }
            contentType(ContentType.Application.Json)
        }.body<TokenResponse>()

        assertEquals(
            actual = tokenResponse.ttlMillis,
            expected = 3_600_000,
        )

        val verifyResponse = client.put("/firebase/token:verify") {
            header(HttpHeaders.AppCheckToken, tokenResponse.token)
        }.body<VerifyResponse>()

        assertEquals(
            expected = verifyResponse.appId,
            actual = verifyResponse.subject,
        )
    }
}

@KtorDsl
private fun testMainApplication(
    configuration: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = DefaultHttpConfig,
    application: Application.() -> Unit = { main() },
    block: suspend ApplicationTestBuilder.(HttpClient) -> Unit,
) = testApplication {
    val client = createClient(configuration)
    application(application)
    block(client)
}

@Serializable
private data class TokenResponse(
    val ttlMillis: Long,
    val token: String,
)

@Serializable
private data class VerifyResponse(
    val audience: List<String>,
    val expiresAt: Long,
    val subject: String,
    val issuedAt: Long,
    val issuer: String,
    val appId: String,
)
