package io.ashdavies.cloud

import io.ashdavies.check.AppCheckRequest
import io.ashdavies.check.AppCheckToken
import io.ashdavies.check.DecodedToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

internal class TokenTest {

    private val mobileSdkAppId = System.getenv("MOBILE_SDK_APP_ID")
    private val playgroundApiKey = System.getenv("PLAYGROUND_API_KEY")

    @Test
    @Ignore("Unable to deploy to production")
    fun `should create production app check token`() = runBlocking {
        val client = HttpClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val bodyAsText = client.post("https://playground.ashdavies.dev/token") {
            headers { append("X-API-Key", playgroundApiKey) }
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(mobileSdkAppId))
        }.bodyAsText()

        println("=== Body ===")
        println(bodyAsText)

        val appCheckToken = Json.decodeFromString<AppCheckToken.Response.Normalised>(bodyAsText)

        val decodedToken = client.put("/verify") {
            header("X-Firebase-AppCheck", appCheckToken.token)
        }.body<DecodedToken>()

        assertEquals(
            expected = decodedToken.appId,
            actual = decodedToken.subject,
        )
    }

    @Test
    fun `should return app check token for request`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val httpResponse = client.post("/token") {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(mobileSdkAppId))
        }

        val appCheckToken = httpResponse.body<AppCheckToken.Response.Normalised>()

        assertEquals(
            actual = appCheckToken.ttlMillis,
            expected = 3_600_000,
        )
    }

    @Test
    fun `should return bad request with missing body`() = testApplication {
        val response = client.post("/token") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
