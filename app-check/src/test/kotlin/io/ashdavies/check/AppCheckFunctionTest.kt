package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import com.google.firebase.FirebaseApp
import io.ashdavies.http.DefaultHttpClient
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.LocalHttpFunction
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class AppCheckFunctionTest {

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `should request bearer tokens in coroutine scope`() = runTest {
        val firebaseApp = FirebaseApp.initializeApp()
        val client = DefaultHttpClient()

        val signer = CryptoSigner(firebaseApp, client)
        val algorithm = GoogleAlgorithm(signer)

        val jwt = Jwt.create(algorithm) {
            it.appId = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))
            it.audience = GOOGLE_TOKEN_ENDPOINT
            it.scope = FIREBASE_CLAIMS_SCOPES
            it.issuer = signer.getAccountId()
        }

        val result = client.post(GOOGLE_TOKEN_ENDPOINT) {
            contentType(ContentType.Application.FormUrlEncoded)
            grantType(JwtBearer)
            assertion(jwt)
        }

        val response = result
            .body<BearerResponse>()
            .accessToken

        assertNotNull(response)
    }

    /**
     * ComparisonFailure: expected:<[Hello World]> but was:<[Compose Runtime internal error.
     * Unexpected or incorrect use of the Compose internal runtime API (Start/end imbalance).
     * Please report to Google or use https://goo.gle/compose-feedback]>
     */
    @Test
    fun `should load bearer tokens for unauthorised http client`() {
        test<TestUnauthorisedApplication> { assertNotNull(it) }
    }

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

internal class TestUnauthorisedApplication : HttpFunction by LocalHttpFunction({ request, response ->
    val app = FirebaseApp.initializeApp()
    val query = AppCheckQuery(request)
    val client = DefaultHttpClient()

    val signer = CryptoSigner(app, client)
    val algorithm = GoogleAlgorithm(signer)

    val accessToken = runBlocking {
        val jwt = Jwt.create(algorithm) {
            it.audience = GOOGLE_TOKEN_ENDPOINT
            it.scope = FIREBASE_CLAIMS_SCOPES
            it.issuer = signer.getAccountId()
            it.appId = query.appId
        }.also(::println)

        val result = client.post(GOOGLE_TOKEN_ENDPOINT) {
            contentType(ContentType.Application.FormUrlEncoded)
            grantType(JwtBearer)
            assertion(jwt)
        }.also(::println)

        result
            .body<BearerResponse>()
            .accessToken
    }

    response.writer.write(accessToken)
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
