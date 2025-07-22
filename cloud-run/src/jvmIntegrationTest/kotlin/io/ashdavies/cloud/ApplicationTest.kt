package io.ashdavies.cloud

import io.ashdavies.check.AppCheckToken
import io.ashdavies.http.common.models.AppCheckToken
import io.ashdavies.http.common.models.DecodedToken
import io.ashdavies.http.common.models.ApiConference
import io.ashdavies.http.common.models.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.utils.io.KtorDsl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

private val DefaultHttpConfig: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
    install(ContentNegotiation, ContentNegotiationConfig::json)
}

@ExperimentalCoroutinesApi
internal class ApplicationTest {

    @Test
    fun `should sign in with custom token`() = testMainApplication { client ->
        val authResult = client.post("/firebase/auth") {
            header("X-API-Key", requireNotNull(BuildConfig.INTEGRATION_API_KEY))
            setBody(mapOf("uid" to "jane.smith@example.com"))
            contentType(ContentType.Application.Json)
        }.body<Map<String, String>>()

        assertNotNull(authResult["idToken"])
    }

    @Test
    fun `should return app check token for request`() = testMainApplication { client ->
        val token = client.post("/firebase/token") {
            setBody(FirebaseApp(requireNotNull(BuildConfig.FIREBASE_ANDROID_APP_ID)))
            contentType(ContentType.Application.Json)
        }.body<AppCheckToken>()

        assertEquals(60.minutes.inWholeMilliseconds, token.ttlMillis)

        val verify = client.put("/firebase/token:verify") {
            header(HttpHeaders.AppCheckToken, token.token)
        }.body<DecodedToken>()

        assertEquals(verify.appId, verify.subject)

        val apiConferences = client.get("/events/upcoming") {
            contentType(ContentType.Application.Json)
        }.body<List<ApiConference>>()

        assertTrue(apiConferences.isNotEmpty())
    }

    @Test
    fun `should aggregate github events`() = testMainApplication { client ->
        val response = client.post("/events:aggregate") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
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
