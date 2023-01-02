package io.ashdavies.cloud

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
internal class AuthTest {

    @Test
    fun `should sign in with custom token`() = testApplication {
        val client = createClient { install(ContentNegotiation, ContentNegotiation.Config::json) }

        val authResult = client.post("/auth") {
            header("X-API-Key", "AIzaSyAhRyznWxQxO1Yd2RkQken7GUFA0IT9P6w")
            setBody(mapOf("uid" to "jane.smith@example.com"))
            contentType(ContentType.Application.Json)
        }.body<Map<String, String>>()

        assertNotNull(authResult["idToken"])
    }
}
