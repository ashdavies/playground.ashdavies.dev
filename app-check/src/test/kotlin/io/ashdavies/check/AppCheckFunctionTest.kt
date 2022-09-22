package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AppCheckFunctionTest {

    private val mobileSdkAppId get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))
    private val appCheckKey get() = requireNotNull(System.getenv("APP_CHECK_KEY"))

    @Test
    fun `should load bearer tokens with config`() = startServer<TestHttpApplication> { client ->
        assertNotEquals("", client.request { it.bodyAsText() })
    }

    @Test
    fun `should validate authorised http application`() = startServer<TestAuthorizedHttpApplication> { client ->
        assertEquals("Hello World", client.request { it.bodyAsText() })
    }

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        assertEquals(HttpStatusCode.Forbidden, client.request { it.status })
    }

    private suspend fun <T> HttpClient.request(
        appId: String = mobileSdkAppId,
        appKey: String = appCheckKey,
        transform: suspend (HttpResponse) -> T,
    ): T = get {
        parameter("appId", appId)
        parameter("appKey", appKey)
    }.let { transform(it) }
}

internal class TestHttpApplication : HttpFunction by HttpApplication({
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current

    HttpEffect { client.getBearerTokens(config).accessToken }
})

internal class TestAuthorizedHttpApplication : HttpFunction by AuthorisedHttpApplication({
    HttpEffect { "Hello World" }
})
