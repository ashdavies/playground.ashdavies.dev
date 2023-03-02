package io.ashdavies.cloud

import io.ashdavies.check.AppCheckToken
import io.ashdavies.check.DecodedToken
import io.ashdavies.playground.models.FirebaseApp
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

private val mobileSdkAppId = System.getenv("MOBILE_SDK_APP_ID")

internal class TokenTest {

    @Test
    fun `should return app check token for request`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val httpResponse = client.post("/firebase/token") {
            // headers { append("X-API-Key", playgroundApiKey) }
            contentType(ContentType.Application.Json)
            setBody(FirebaseApp(mobileSdkAppId))
        }

        val appCheckToken = httpResponse.body<AppCheckToken.Response.Normalised>()

        assertEquals(
            actual = appCheckToken.ttlMillis,
            expected = 3_600_000,
        )

        val decodedToken = client.put("/firebase/token:verify") {
            header("X-Firebase-AppCheck", appCheckToken.token)
        }.body<DecodedToken>()

        assertEquals(
            expected = decodedToken.appId,
            actual = decodedToken.subject,
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
