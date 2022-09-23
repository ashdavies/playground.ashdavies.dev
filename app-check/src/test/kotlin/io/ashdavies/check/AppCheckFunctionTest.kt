package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AppCheckFunctionTest {

    @Test
    fun `should load bearer tokens for unauthorised http client`() = test<TestUnauthorisedApplication>()

    /**
     * ComparisonFailure: expected:<[Hello World]> but was:<[Compose Runtime internal error.
     * Unexpected or incorrect use of the Compose internal runtime API (Start/end imbalance).
     * Please report to Google or use https://goo.gle/compose-feedback]>
     */
    @Test
    fun `should load bearer tokens for authorised http client`() = test<TestAuthorisedApplication>()

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

internal class TestUnauthorisedApplication : HttpFunction by HttpApplication({
    val config = rememberHttpClientConfig().also(::println)
    val client = LocalHttpClient.current

    HttpEffect {
        val jwt = Jwt.create(config.algorithm) {
            it.audience = GOOGLE_TOKEN_ENDPOINT
            it.scope = FIREBASE_CLAIMS_SCOPES
            it.issuer = config.accountId
            it.appId = config.appId
        }.also(::println)

        val response = client.post(GOOGLE_TOKEN_ENDPOINT) {
            contentType(ContentType.Application.FormUrlEncoded)
            grantType(JwtBearer)
            assertion(jwt)
        }.also(::println)

        response
            .body<BearerResponse>()
            .accessToken
            .substring(0..240)
            .also(::println)
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
