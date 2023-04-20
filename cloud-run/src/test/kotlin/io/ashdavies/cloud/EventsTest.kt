package io.ashdavies.cloud

import io.ashdavies.playground.models.Event
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

internal class EventsTest {

    @Test
    @Ignore
    fun `should get events with default limit`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }
        val response = client.get("/events") { contentType(ContentType.Application.Json) }
        val body = response.body<List<Event>>()

        assertEquals(50, body.size)
    }

    @Test
    fun `should aggregate events`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }
        val response = client.post("/events:aggregate")

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
