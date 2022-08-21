package io.ashdavies.check

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

internal class AppCheckFunctionTest {

    private val serviceAccountId get() = requireNotNull(System.getenv("SERVICE_ACCOUNT_ID"))
    private val appCheckKey get() = requireNotNull(System.getenv("APP_CHECK_KEY"))

    @Test
    fun `app attestation should fail`() = startServer<AppCheckFunction> { client ->
        val response: HttpResponse = client.get {
            parameter("appId", serviceAccountId)
            parameter("appKey", appCheckKey)
        }

        assertEquals(
            HttpStatusCode.Forbidden,
            response.status,
        )
    }
}
