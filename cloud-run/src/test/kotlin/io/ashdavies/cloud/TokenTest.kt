package io.ashdavies.cloud

import io.ashdavies.http.AppCheckToken
import io.ashdavies.playground.models.FirebaseApp
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

private val mobileSdkAppId = System.getenv("MOBILE_SDK_APP_ID")

internal class TokenTest {

    @Test
    fun `should return app check token for request`() = testApplication {
        val httpClient = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val tokenResponse = httpClient.post("/firebase/token") {
            // headers { append("X-API-Key", playgroundApiKey) }
            contentType(ContentType.Application.Json)
            setBody(FirebaseApp(mobileSdkAppId))
        }.body<TokenResponse>()

        assertEquals(
            actual = tokenResponse.ttlMillis,
            expected = 3_600_000,
        )

        val verifyResponse = httpClient.put("/firebase/token:verify") {
            header(HttpHeaders.AppCheckToken, tokenResponse.token)
        }.body<VerifyResponse>()

        assertEquals(
            expected = verifyResponse.appId,
            actual = verifyResponse.subject,
        )
    }

    @Test
    fun `should return bad request with missing body`() = testApplication {
        val response = client.post("/firebase/token") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
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
