package io.ashdavies.cloud

import io.ashdavies.http.AppCheckToken
import io.ashdavies.playground.models.AppCheckToken
import io.ashdavies.playground.models.DecodedToken
import io.ashdavies.playground.models.Event
import io.ashdavies.playground.models.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.util.KtorDsl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val DefaultHttpConfig: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
    install(ContentNegotiation, ContentNegotiation.Config::json)
}

@ExperimentalCoroutinesApi
internal class ApplicationTest {

    @Test
    fun `should sign in with custom token`() = testMainApplication { client ->
        val authResult = client.post("/firebase/auth") {
            header("X-API-Key", requireNotNull(System.getenv("GOOGLE_PROJECT_API_KEY")))
            setBody(mapOf("uid" to "jane.smith@example.com"))
            contentType(ContentType.Application.Json)
        }.body<Map<String, String>>()

        assertNotNull(authResult["idToken"])
    }

    @Test
    fun `should return app check token for request`() = testMainApplication { client ->
        val token = client.post("/firebase/token") {
            setBody(FirebaseApp(System.getenv("MOBILE_SDK_APP_ID")))
            contentType(ContentType.Application.Json)
        }.body<AppCheckToken>()

        assertEquals(3_600_000, token.ttlMillis)

        val verify = client.put("/firebase/token:verify") {
            header(HttpHeaders.AppCheckToken, token.token)
        }.body<DecodedToken>()

        assertEquals(verify.appId, verify.subject)

        val events = client.get("/events") {
            contentType(ContentType.Application.Json)
        }.body<List<Event>>()

        assertEquals(50, events.size)
    }
}

@KtorDsl
private fun testMainApplication(
    configuration: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = DefaultHttpConfig,
    application: Application.() -> Unit = { main() },
    block: suspend ApplicationTestBuilder.(HttpClient) -> Unit,
) = testApplication {
    val client = createClient(configuration)
    application(application)
    block(client)
}
