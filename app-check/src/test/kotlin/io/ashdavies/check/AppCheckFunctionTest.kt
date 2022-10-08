package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import io.ashdavies.playground.cloud.HttpEffect
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppCheckFunctionTest {

    @Test
    fun `should load bearer tokens for authorised http client`() = runTest<TestAuthorisedApplication>()

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        assertEquals(HttpStatusCode.OK, client.makeRequest { it.status })
    }
}

internal class TestAuthorisedApplication : HttpFunction by AuthorisedHttpApplication({
    HttpEffect { "Hello World" }
})

@ExperimentalCoroutinesApi
private inline fun <reified T : HttpFunction> runTest(
    noinline block: (actual: String) -> Unit = { assertEquals("Hello World", it) }
) = startServer<T> { client -> block(client.makeRequest { it.bodyAsText() }) }

private suspend fun <T> HttpClient.makeRequest(
    appId: String = requireNotNull(System.getenv("MOBILE_SDK_APP_ID")),
    appKey: String = requireNotNull(System.getenv("APP_CHECK_KEY")),
    transform: suspend (HttpResponse) -> T,
): T = get {
    parameter("appId", appId)
    parameter("appKey", appKey)
}.let { transform(it) }
