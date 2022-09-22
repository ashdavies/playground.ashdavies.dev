package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import kotlin.test.assertEquals

internal class AppCheckFunctionTest {

    private val mobileSdkAppId get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))
    private val appCheckKey get() = requireNotNull(System.getenv("APP_CHECK_KEY"))

    @Test
    fun `app attestation should fail`() = startServer<AppCheckFunction> { client ->
        val response: HttpResponse = client.get {
            parameter("appId", mobileSdkAppId)
            parameter("appKey", appCheckKey)
        }

        assertEquals(
            HttpStatusCode.Forbidden,
            response.status,
        )
    }

    @Test
    fun `test function should succeed`() {
        startServer<TestAppCheckFunction> { client ->
            assertNotNull(client.get("https://www.greetingsapi.com/random"))
        }
    }
}

private class TestAppCheckFunction : HttpFunction by AuthorizedHttpApplication({
    println("Hello World")
})
