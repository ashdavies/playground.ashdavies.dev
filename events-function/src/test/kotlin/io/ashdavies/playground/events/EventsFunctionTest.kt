package io.ashdavies.playground.events

import io.ashdavies.cloud.startServer
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class EventsFunctionTest {

    @Test
    @Ignore("Ignore until Playground server is available")
    fun `should deny request without app check token`() = startServer<EventsFunction> { client ->
        assertEquals(HttpStatusCode.Unauthorized, client.get { }.status)
    }

    @Test
    fun `should return headers for method not allowed`() = startServer<EventsFunction> { client ->
        val response: HttpResponse = client.post { contentType(ContentType.Application.Json) }

        assertEquals(HttpMethod.Get.value, response.headers[HttpHeaders.Allow])
        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)
    }
}
