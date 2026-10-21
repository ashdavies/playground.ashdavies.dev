package dev.ashdavies.cloud

import dev.ashdavies.http.common.models.ApiConference
import dev.zacsweers.metro.createGraph
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

private val DefaultHttpConfig: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
}

@ExperimentalCoroutinesApi
internal class ApplicationTest {

    @Test
    fun `should fetch upcoming events`() = testMainApplication { client ->
        val appCheckToken = HttpClient(DefaultHttpConfig)
            .createAppCheckToken()
            .token

        val response = client.get("/events/upcoming") {
            headers["X-Firebase-AppCheck"] = appCheckToken
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<List<ApiConference>>()
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .date

        assertTrue(body.isNotEmpty())

        body.forEach {
            assertTrue(LocalDate.parse(it.dateStart) > today)
        }
    }

    @Test
    @Ignore
    fun `should aggregate github events`() = testMainApplication { client ->
        val response = client.post("/events:aggregate") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}

private fun testMainApplication(block: suspend ApplicationTestBuilder.(HttpClient) -> Unit) = testApplication {
    val client = createClient(DefaultHttpConfig)
    val graph = createGraph<CloudRunGraph>()
    application { main(graph.routes) }
    block(client)
}
