package io.ashdavies.gallery

import app.cash.turbine.test
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.tally.files.Path
import io.ashdavies.tally.gallery.Image
import io.ashdavies.tally.gallery.SyncManager
import io.ashdavies.tally.gallery.SyncState
import io.ashdavies.tally.gallery.inMemoryHttpClientEngine
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

internal class SyncManagerTest {

    @Test
    fun `should request initial value`() = runTest {
        val uuid = Uuid.random()

        val manager = SyncManager(
            httpClient = inMemoryHttpClient(listOf("$uuid")),
            fileManager = InMemoryFileManager(),
        )

        manager.state.test {
            assertEquals(mapOf(uuid to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should sync image on invocation`() = runTest {
        val uuid = Uuid.random()
        val path = Path("$uuid")

        val manager = SyncManager(
            httpClient = inMemoryHttpClient(),
            fileManager = InMemoryFileManager(path),
        )

        manager.state.test {
            assertEquals(emptyMap(), awaitItem())

            manager.sync(Image(uuid, path))

            assertEquals(mapOf(uuid to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(uuid to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should put synced image without content`() = runTest {
        val uuid = Uuid.random()
        val path = Path("$uuid")

        val manager = SyncManager(
            httpClient = inMemoryHttpClient(listOf("$uuid")),
            fileManager = InMemoryFileManager()
        )

        manager.state.test {
            assertEquals(mapOf(uuid to SyncState.SYNCED), awaitItem())

            manager.sync(Image(uuid, path))

            assertEquals(mapOf(uuid to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(uuid to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should include content length header`() = runTest {
        val uuid = Uuid.random()
        val path = Path("$uuid")

        val manager = SyncManager(
            httpClient = inMemoryHttpClient(),
            fileManager = InMemoryFileManager(path),
        )

        manager.state.test {
            manager.sync(Image(uuid, path))

            cancelAndIgnoreRemainingEvents()
        }
    }
}

private fun inMemoryHttpClient(initialValue: List<String> = emptyList()) = HttpClient(
    engine = inMemoryHttpClientEngine(initialValue),
    block = DefaultHttpConfiguration,
)
