package io.ashdavies.cloud

import io.ashdavies.check.AppCheckRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

internal class TokenTest {

    private val mobileSdkAppId = System.getenv("MOBILE_SDK_APP_ID")
    private val playgroundApiKey = System.getenv("PLAYGROUND_API_KEY")

    @Test
    @Ignore("Endpoint cannot be queried before deployment")
    fun `should create production app check token`() = runBlocking {
        val client = HttpClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val response = client.post("https://playground.ashdavies.dev/token") {
            headers { append("X-API-Key", playgroundApiKey) }
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(mobileSdkAppId))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should return app check token for request`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val response = client.post("/token") {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(mobileSdkAppId))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Ignore("DEBUGGING")
    fun `should return bad request with missing body`() = testApplication {
        val response = client.post("/token") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    @Ignore("DEBUGGING")
    fun `should return headers for method not allowed`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val response = client.get("/token") {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(mobileSdkAppId))
        }

        assertEquals(HttpMethod.Post.value, response.headers[HttpHeaders.Allow])
        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)
    }

    @Test
    @Ignore("DEBUGGING")
    fun `should return headers for unsupported media type`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val response = client.post("/token") {
            setBody("<AppCheckRequest appId=\"$mobileSdkAppId\" />")
            contentType(ContentType.Application.Xml)
        }

        assertEquals("${ContentType.Application.Json}", response.headers[HttpHeaders.Accept])
        assertEquals(HttpStatusCode.UnsupportedMediaType, response.status)
    }
}
