package io.ashdavies.check

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.functions.HttpFunction
import com.google.firebase.FirebaseApp
import io.ashdavies.http.DefaultHttpClient
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

private val mobileSdkAppId = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))
private val appCheckKey = requireNotNull(System.getenv("APP_CHECK_KEY"))

internal class AppCheckFunctionTest {

    @Test
    fun `should get access token`() = test<TestGetAccessTokenApplication> { assertNotEquals("", it) }

    @Test
    fun `should get default response with sample bearer`() = test<TestSampleTokensApplication>()

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `should make coroutine bearer token request`() = runTest {
        val firebaseApp = FirebaseApp.initializeApp()

        val serviceAccountId = requireNotNull(findExplicitServiceAccountId(firebaseApp))
        val client = DefaultHttpClient()

        val signer = CryptoSigner(
            serviceAccountId = serviceAccountId,
            firebaseApp = firebaseApp,
            client = client,
        )

        val config = HttpClientConfig(
            algorithm = GoogleAlgorithm(signer),
            accountId = signer.getAccountId(),
            appId = mobileSdkAppId,
        )

        try {
            val urlString = "https://firebaseappcheck.googleapis.com/v1/projects"
            val token: String = client.getBearerTokens(config).accessToken

            val request = client.get(urlString) { header(HttpHeaders.Authorization, "Bearer $token") }
            val response = request.bodyAsText()

            println(response)
            assertNotNull(response)
        } catch (exception: HttpException) {
            println(exception)
            assertEquals(401, exception.code)
        }
    }

    private fun CryptoSigner(firebaseApp: FirebaseApp, client: HttpClient, serviceAccountId: String): CryptoSigner {
        return when (val credentials: GoogleCredentials = firebaseApp.credentials) {
            is ServiceAccountSigner -> CryptoSigner(credentials.account, credentials::sign)
            else -> IamSigner(client, serviceAccountId)
        }
    }

    private fun IamSigner(client: HttpClient, serviceAccountId: String): CryptoSigner {
        val urlString = "https://iamcredentials.googleapis.com/v1/projects/-/serviceAccounts/$serviceAccountId:signBlob"
        val encoder = Base64.getEncoder()

        return CryptoSigner(serviceAccountId) {
            client.post(urlString, mapOf("payload" to encoder.encodeToString(it)))
        }
    }

    @Test
    fun `should append bearer tokens manually`() = test<TestManualBearerTokenApplication>()

    @Test
    fun `should load tokens for client`() = test<TestLoadTokensApplication>()

    @Test
    fun `should get hello world when authorised`() = test<TestAuthorisedApplication>()

    @Test
    fun `should execute app check action when authorised`() = test<TestAppCheckActionApplication>()

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        assertEquals(HttpStatusCode.Forbidden, client.request { it.status })
    }
}

internal class TestGetAccessTokenApplication : HttpFunction by HttpApplication({
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current

    HttpEffect { client.getBearerTokens(config).accessToken }
})

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

internal class TestManualBearerTokenApplication : HttpFunction by HttpApplication({
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current

    HttpEffect {
        val token: String = client
            .getBearerTokens(config)
            .accessToken

        val response = client.get("https://firebaseappcheck.googleapis.com/v1/projects") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.bodyAsText()

        if (response.isNotEmpty()) "Hello World" else ""
    }
})

internal class TestLoadTokensApplication : HttpFunction by HttpApplication({
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current
    val loader = DefaultHttpClient()

    val authorised = remember(client) {
        client.config {
            install(Auth) {
                bearer { loadTokens { loader.getBearerTokens(config) } }
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

private inline fun <reified T : HttpFunction> test(
    noinline block: (actual: String) -> Unit = { assertEquals("Hello World", it) }
) = startServer<T> { client -> block(client.request { it.bodyAsText() }) }

private suspend fun <T> HttpClient.request(
    appId: String = mobileSdkAppId,
    appKey: String = appCheckKey,
    transform: suspend (HttpResponse) -> T,
): T = get {
    parameter("appId", appId)
    parameter("appKey", appKey)
}.let { transform(it) }
