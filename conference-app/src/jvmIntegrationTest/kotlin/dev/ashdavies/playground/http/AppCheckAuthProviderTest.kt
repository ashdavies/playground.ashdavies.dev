package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.AppCheckToken
import dev.ashdavies.http.common.models.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppCheckAuthProviderTest {

    @Test
    fun `should acquire token preemptively`() = runAppCheckTest(
        handler = { request, _ ->
            val path = request.url.encodedPath
            when {
                path.endsWith("/firebase/token") -> {
                    val bodyText = when (val body = request.body) {
                        is OutgoingContent.ByteArrayContent -> String(body.bytes())
                        else -> ""
                    }
                    val firebaseApp = json.decodeFromString<FirebaseApp>(bodyText)
                    assertEquals("test-app-id", firebaseApp.appId)

                    respond(
                        content = json.encodeToString(AppCheckToken(ttlMillis = 3600000, token = "test-token")),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }
                path.endsWith("/events/upcoming") -> respond(
                    content = "[]",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
                else -> respond("", HttpStatusCode.NotFound)
            }
        }
    ) { client, requestHistory ->
        client.get("https://localhost/events/upcoming")

        assertEquals(2, requestHistory.size)
        assertTrue(requestHistory[0].url.encodedPath.endsWith("/firebase/token"))
        assertTrue(requestHistory[1].url.encodedPath.endsWith("/events/upcoming"))
        assertEquals("test-token", requestHistory[1].headers["X-Firebase-AppCheck"])
    }

    @Test
    fun `should refresh token when server challenges with 401`() = runAppCheckTest(
        handler = { request, history ->
            val path = request.url.encodedPath
            when {
                path.endsWith("/firebase/token") -> {
                    val isFirstTokenCall = history.none { it.url.encodedPath.endsWith("/firebase/token") }
                    val token = if (isFirstTokenCall) "token-1" else "token-2"
                    respond(
                        content = json.encodeToString(AppCheckToken(ttlMillis = 3600000, token = token)),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }
                path.endsWith("/events/upcoming") -> {
                    val header = request.headers["X-Firebase-AppCheck"]
                    if (header == "token-1") {
                        respond(
                            content = "Unauthorized",
                            status = HttpStatusCode.Unauthorized,
                            headers = headersOf(HttpHeaders.WWWAuthenticate, "AppCheck realm=\"Firebase App Check\"")
                        )
                    } else {
                        respond(
                            content = "[]",
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )
                    }
                }
                else -> respond("", HttpStatusCode.NotFound)
            }
        }
    ) { client, requestHistory ->
        client.get("https://localhost/events/upcoming")

        assertEquals(4, requestHistory.size)
        assertEquals("token-1", requestHistory[1].headers["X-Firebase-AppCheck"])
        assertEquals("token-2", requestHistory[3].headers["X-Firebase-AppCheck"])
    }

    private fun runAppCheckTest(
        handler: suspend MockRequestHandleScope.(HttpRequestData, List<HttpRequestData>) -> HttpResponseData,
        block: suspend (HttpClient, List<HttpRequestData>) -> Unit
    ) = runTest {
        val requestHistory = mutableListOf<HttpRequestData>()
        val mockEngine = MockEngine { request ->
            val response = handler(this, request, requestHistory)
            requestHistory.add(request)
            response
        }

        val tokenClient = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
            install(Auth) {
                providers.add(createAppCheckAuthProvider("test-app-id", tokenClient))
            }
        }

        block(client, requestHistory)
    }
}
