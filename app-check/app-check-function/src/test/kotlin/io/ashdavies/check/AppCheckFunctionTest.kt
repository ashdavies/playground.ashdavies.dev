package io.ashdavies.check

import io.ashdavies.cloud.TestHttpClient
import io.ashdavies.cloud.startServer
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

private const val CREATE_TOKEN_FUNCTION: String =
    "https://playground.ashdavies.dev/token"

private val MOBILE_SDK_APP_ID: String
    get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppCheckFunctionTest {

    @Test
    @Ignore("Ignore until Playground server is available")
    fun `should create production app check token`() = runBlocking {
        val client = TestHttpClient(CREATE_TOKEN_FUNCTION)
        val response: HttpResponse = client.post {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(MOBILE_SDK_APP_ID))
        }

        // Function can only be accessed with permission
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Ignore("Ignore until Playground server is available")
    fun `should return app check token for request`() = startServer<AppCheckFunction> { client ->
        val response: HttpResponse = client.post {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(MOBILE_SDK_APP_ID))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should return bad request with missing body`() = startServer<AppCheckFunction> { client ->
        val response: HttpResponse = client.post { contentType(ContentType.Application.Json) }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `should return headers for method not allowed`() = startServer<AppCheckFunction> { client ->
        val response: HttpResponse = client.get {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(MOBILE_SDK_APP_ID))
        }

        assertEquals(HttpMethod.Post.value, response.headers[HttpHeaders.Allow])
        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)
    }

    @Test
    fun `should return header unsupported media type`() = startServer<AppCheckFunction> { client ->
        val response: HttpResponse = client.post {
            setBody(xml(AppCheckRequest(MOBILE_SDK_APP_ID)))
            contentType(ContentType.Application.Xml)
        }

        assertEquals("${ContentType.Application.Json}", response.headers[HttpHeaders.Accept])
        assertEquals(HttpStatusCode.UnsupportedMediaType, response.status)
    }
}

private fun xml(request: AppCheckRequest) = """
<?xml version="1.0" encoding="utf-8"?>
<AppCheckRequest appId="${request.appId}" />
""".trimIndent()
