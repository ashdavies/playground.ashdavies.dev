package io.ashdavies.cloud

import io.ashdavies.http.AppCheckToken
import io.ashdavies.playground.models.FirebaseApp
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TokenTest {

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
