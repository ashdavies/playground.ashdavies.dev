package io.ashdavies.check

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppCheckFunctionTest {

    private val mobileSdkAppId: String
        get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        val request: HttpResponse = client.get { parameter("appId", mobileSdkAppId) }

        assertEquals(HttpStatusCode.OK, request.status)
    }
}
