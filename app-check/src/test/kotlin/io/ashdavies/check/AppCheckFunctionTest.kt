package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import io.ashdavies.playground.cloud.HttpEffect
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

internal class AppCheckFunctionTest {

    private val mobileSdkAppId get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))
    private val appCheckKey get() = requireNotNull(System.getenv("APP_CHECK_KEY"))

    @Test
    fun `should validate bearer token`() = startServer<TestAppCheckFunction> { client ->
        val response = client.get { parameter("appId", mobileSdkAppId) }
        assertEquals("Hello World", response.bodyAsText())
    }

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        val response: HttpResponse = client.get {
            parameter("appId", mobileSdkAppId)
            parameter("appKey", appCheckKey)
        }

        assertEquals(
            HttpStatusCode.Forbidden,
            response.status,
        )
    }
}

internal class TestAppCheckFunction : HttpFunction by AuthorizedHttpApplication({
    HttpEffect { "Hello World" }
})
