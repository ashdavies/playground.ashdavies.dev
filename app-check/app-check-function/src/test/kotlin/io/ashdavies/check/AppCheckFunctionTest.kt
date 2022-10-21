package io.ashdavies.check

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppCheckFunctionTest {

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        val mobileSdkAppId = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

        val response = client.post {
            contentType(ContentType.Application.Json)
            setBody(AppCheckRequest(mobileSdkAppId))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
