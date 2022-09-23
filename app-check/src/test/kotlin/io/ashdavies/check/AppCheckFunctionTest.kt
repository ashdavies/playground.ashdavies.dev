package io.ashdavies.check

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpFunction
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AppCheckFunctionTest {

    @Test
    fun `should get access token`() = test<TestGetAccessTokenApplication> { assertNotEquals("", it) }

    @Test
    fun `should load tokens for client`() = test<TestLoadTokensApplication>()

    @Test
    fun `should get hello world when authorised`() = test<TestAuthorisedApplication>()

    @Test
    fun `should execute app check action when authorised`() = test<TestAppCheckActionApplication>()

    @Test
    @Ignore("Diagnostic checks required to discover cause of failure")
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        assertEquals(HttpStatusCode.Forbidden, client.request { it.status })
    }
}

internal class TestGetAccessTokenApplication : HttpFunction by HttpApplication({
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current

    HttpEffect { client.getBearerTokens(config).accessToken }
})

internal class TestAppCheckActionApplication : HttpFunction by HttpApplication({
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current

    val authorised = remember(client) {
        client.config {
            install(Auth) {
                bearer { loadTokens { client.getBearerTokens(config) } }
            }
        }
    }

    CompositionLocalProvider(LocalHttpClient provides authorised) {
        AppCheckAction()
    }
})

internal class TestLoadTokensApplication : HttpFunction by HttpApplication({
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current

    val authorised = remember(client) {
        client.config {
            install(Auth) {
                bearer { loadTokens { client.getBearerTokens(config) } }
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
