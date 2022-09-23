package io.ashdavies.check

import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpFunction
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AppCheckFunctionTest {

    @Test
    fun `should get default response with sample bearer`() = test<TestSampleTokensApplication>()

    /**
     * ComparisonFailure: expected:<[Hello World]> but was:<[Compose Runtime internal error.
     * Unexpected or incorrect use of the Compose internal runtime API (Start/end imbalance).
     * Please report to Google or use https://goo.gle/compose-feedback]>
     */
    @Test
    fun `should get hello world when authorised`() = test<TestAuthorisedApplication>()

    /**
     * AssertionError: expected:<403 Forbidden> but was:<500 Compose Runtime internal error.
     * Unexpected or incorrect use of the Compose internal runtime API (Start/end imbalance).
     * Please report to Google or use https://goo.gle/compose-feedback>
     */
    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        assertEquals(HttpStatusCode.Forbidden, client.request { it.status })
    }
}

internal class TestSampleTokensApplication : HttpFunction by HttpApplication({
    val client: HttpClient = LocalHttpClient.current

    val authorised = remember(client) {
        client.config {
            install(Auth) {
                bearer {
                    loadTokens { BearerTokens("abc123", "xyz111") }
                }
            }
        }
    }

    HttpEffect {
        val response = authorised
            .get("https://api.github.com/")
            .bodyAsText()

        if (response.isNotEmpty()) "Hello World" else ""
    }
})

internal class TestAuthorisedApplication : HttpFunction by AuthorisedHttpApplication({
    HttpEffect { "Hello World" }
})

private inline fun <reified T : HttpFunction> test(
    noinline block: (actual: String) -> Unit = { assertEquals("Hello World", it) }
) = startServer<T> { client -> block(client.request { it.bodyAsText() }) }

private suspend fun <T> HttpClient.request(
    appId: String = requireNotNull(System.getenv("MOBILE_SDK_APP_ID")),
    appKey: String = requireNotNull(System.getenv("APP_CHECK_KEY")),
    transform: suspend (HttpResponse) -> T,
): T = get {
    parameter("appId", appId)
    parameter("appKey", appKey)
}.let { transform(it) }
