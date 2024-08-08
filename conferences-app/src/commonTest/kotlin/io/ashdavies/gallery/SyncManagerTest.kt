package io.ashdavies.gallery

import app.cash.turbine.test
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.util.randomUuid
import io.ktor.client.HttpClient
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val RandomImage = "${randomUuid()}.jpg"

internal class SyncManagerTest {

    @Test
    fun `should request initial value`() = runTest {
        val manager = SyncManager(
            client = HttpClient(
                engine = inMemoryHttpClientEngine(listOf(RandomImage)),
                block = DefaultHttpConfiguration,
            ),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should sync image on invocation`() = runTest {
        val manager = SyncManager(
            client = HttpClient(
                engine = inMemoryHttpClientEngine(emptyList()),
                block = DefaultHttpConfiguration,
            ),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            assertEquals(emptyMap(), awaitItem())

            manager.sync("resources/$RandomImage")

            assertEquals(mapOf(RandomImage to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should put synced image without content`() = runTest {
        val manager = SyncManager(
            client = HttpClient(
                engine = inMemoryHttpClientEngine(listOf(RandomImage)),
                block = DefaultHttpConfiguration,
            ),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())

            manager.sync("resources/$RandomImage")

            assertEquals(mapOf(RandomImage to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should include content length header`() = runTest {
        val manager = SyncManager(
            client = HttpClient(
                engine = inMemoryHttpClientEngine(emptyList()),
                block = DefaultHttpConfiguration,
            ),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            manager.sync("resources/$RandomImage")
            cancelAndIgnoreRemainingEvents()
        }
    }
}
