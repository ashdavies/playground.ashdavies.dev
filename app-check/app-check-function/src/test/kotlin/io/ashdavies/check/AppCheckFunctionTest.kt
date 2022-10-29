package io.ashdavies.check

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppCheckFunctionTest {

    @Test
    fun `should return app check token for given app id`() = startServer<AppCheckFunction> { client ->
        val mobileSdkAppId = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

        val response: HttpResponse = client.post {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(mobileSdkAppId))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `should return headers for method not allowed`() = startServer<AppCheckFunction> { client ->
        val mobileSdkAppId = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

        val response = client.get {
            contentType(ContentType.Application.Json)
            parameter("appId", mobileSdkAppId)
        }

        assertEquals(HttpMethod.Post.value, response.headers[HttpHeaders.Allow])
        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)
    }

    @Test
    fun `should return header unsupported media type`() = startServer<AppCheckFunction> { client ->
        val mobileSdkAppId = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

        val response: HttpResponse = client.post {
            setBody(xml(AppCheckRequest(mobileSdkAppId)))
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
