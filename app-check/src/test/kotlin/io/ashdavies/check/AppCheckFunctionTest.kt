package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.junit.Test
import java.net.HttpURLConnection
import kotlin.test.assertEquals

internal class AppCheckFunctionTest {

    private val mobileSdkAppId get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))
    private val appCheckKey get() = requireNotNull(System.getenv("APP_CHECK_KEY"))

    @Test
    fun `test simple function`() = startServer<SimpleHttpFunction> { client ->
        assertEquals("Hello World", client.get { }.bodyAsText())
    }

    @Test
    fun `test simple delegate function`() = startServer<SimpleDelegateHttpFunction> { client ->
        assertEquals("Hello World", client.get { }.bodyAsText())
    }

    @Test
    fun `should validate http application`() = startServer<TestHttpApplication> { client ->
        assertEquals("Hello World", client.get { }.bodyAsText())
    }

    @Test
    fun `should validate authorised http application`() = startServer<TestAuthorizedHttpApplication> { client ->
        val response = client.get { parameter("appId", mobileSdkAppId) }
        assertEquals("Hello World", response.bodyAsText())
    }

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        val response = client.get {
            parameter("appId", mobileSdkAppId)
            parameter("appKey", appCheckKey)
        }

        assertEquals(
            HttpStatusCode.Forbidden,
            response.status,
        )
    }
}

internal class SimpleHttpFunction : HttpFunction {
    override fun service(request: HttpRequest, response: HttpResponse) {
        response.setStatusCode(HttpURLConnection.HTTP_OK)
        response.writer.write("Hello World")
    }
}

internal class SimpleDelegateHttpFunction : HttpFunction by HttpFunction({ _, response ->
    response.setStatusCode(HttpURLConnection.HTTP_OK)
    response.writer.write("Hello World")
})

internal class TestHttpApplication : HttpFunction by HttpApplication({
    HttpEffect { "Hello World" }
})

internal class TestAuthorizedHttpApplication : HttpFunction by AuthorisedHttpApplication({
    HttpEffect { "Hello World" }
})
