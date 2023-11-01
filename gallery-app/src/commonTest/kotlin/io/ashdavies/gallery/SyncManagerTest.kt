package io.ashdavies.gallery

import app.cash.turbine.test
import io.ashdavies.http.DefaultHttpClient
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val RandomImage = "${randomUuid()}.jpg"

internal class SyncManagerTest {

    @Test
    fun `should request initial value`() = runTest {
        val manager = SyncManager(
            client = DefaultHttpClient(InMemoryHttpClientEngine(listOf(RandomImage))),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCED))
        }
    }

    @Test
    fun `should sync image on invocation`() = runTest {
        val manager = SyncManager(
            client = DefaultHttpClient(InMemoryHttpClientEngine(emptyList())),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            skipItems(1) // initialValue = emptyMap()

            manager.sync("resources/$RandomImage")

            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCING))
            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCED))
        }
    }

    @Test
    fun `should put synced image without content`() = runTest {
        val manager = SyncManager(
            client = DefaultHttpClient(InMemoryHttpClientEngine(listOf(RandomImage))),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            skipItems(1) // initialValue = emptyMap()

            manager.sync("resources/$RandomImage")

            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCING))
            assertEquals(awaitItem(), mapOf(RandomImage to SyncState.SYNCED))
        }
    }

    @Test
    fun `should include content length header`() = runTest {
        val manager = SyncManager(
            client = DefaultHttpClient(InMemoryHttpClientEngine(emptyList())),
            reader = { ByteReadChannel.Empty },
        )

        manager.state.test {
            manager.sync("resources/$RandomImage")
            cancelAndIgnoreRemainingEvents()
        }
    }
}
