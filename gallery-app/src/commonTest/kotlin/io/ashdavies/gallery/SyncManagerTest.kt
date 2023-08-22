package io.ashdavies.gallery

import app.cash.turbine.test
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

private val DefaultHeaders = headersOf(HttpHeaders.ContentType, "application/json")
private val RandomImage = "${randomUuid()}.jpg"

internal class SyncManagerTest {

    @Test
    fun `should request initial value`() = runTest {
        val manager = SyncManager(
            MockEngine { request ->
                when {
                    request.method == HttpMethod.Get && request.url.encodedPath == "/" -> {
                        respond(ByteReadChannel("[$RandomImage]"), headers = DefaultHeaders)
                    }

                    else -> fail()
                }
            },
        )

        manager.state().test {
            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCED))
        }
    }

    @Test
    fun `should sync image on invocation`() = runTest {
        val manager = SyncManager(
            MockEngine { request ->
                when {
                    request.method == HttpMethod.Get && request.url.encodedPath == "/" -> {
                        respond(ByteReadChannel("[]"), headers = DefaultHeaders)
                    }

                    request.method == HttpMethod.Post -> {
                        respond(ByteReadChannel.Empty, headers = DefaultHeaders)
                    }

                    else -> fail()
                }
            },
        )

        manager.state().test {
            skipItems(1) // initialValue = emptyMap()

            manager.sync("resources/$RandomImage")

            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCING))
            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCED))
        }
    }

    @Test
    fun `should put synced image without content`() = runTest {
        val manager = SyncManager(
            MockEngine { request ->
                when {
                    request.method == HttpMethod.Get && request.url.encodedPath == "/" -> {
                        respond(ByteReadChannel("[$RandomImage]"), headers = DefaultHeaders)
                    }

                    request.method == HttpMethod.Put -> {
                        respond(ByteReadChannel.Empty, headers = DefaultHeaders)
                    }

                    else -> fail("Unexpected request (${request.method} ${request.url.encodedPath})")
                }
            },
        )

        manager.state().test {
            skipItems(1) // initialValue = emptyMap()

            manager.sync("resources/$RandomImage")

            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCING))
            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCED))
        }
    }

    @Test
    fun `should include content length header`() = runTest {
        val manager = SyncManager(
            MockEngine { request ->
                when {
                    request.method == HttpMethod.Get && request.url.encodedPath == "/" -> {
                        respond(ByteReadChannel("[]"), headers = DefaultHeaders)
                    }

                    request.method == HttpMethod.Post -> {
                        assertNotNull(request.headers[HttpHeaders.ContentLength])
                        respond(ByteReadChannel.Empty, headers = DefaultHeaders)
                    }

                    else -> fail()
                }
            },
        )

        manager.state().test {
            manager.sync("resources/$RandomImage")
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private fun SyncManager(engine: MockEngine) = SyncManager(
    client = HttpClient(engine) {
        install(ContentNegotiation, ContentNegotiation.Config::json)
    },
    reader = { ByteReadChannel.Empty },
)
