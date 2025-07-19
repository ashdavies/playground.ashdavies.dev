package io.ashdavies.gallery

import app.cash.turbine.test
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.tally.files.FileManager
import io.ashdavies.tally.gallery.Image
import io.ashdavies.tally.gallery.SyncManager
import io.ashdavies.tally.gallery.SyncState
import io.ashdavies.tally.gallery.inMemoryHttpClientEngine
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import io.ashdavies.tally.files.Path
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

private val RandomImage = Uuid.random()

internal class SyncManagerTest {

    @Test
    fun `should test wasm`() {
        val path = Path("$RandomImage")
    }

    @Test
    @Ignore
    fun `should request initial value`() = runTest {
        val manager = SyncManager(inMemoryHttpClient(listOf("$RandomImage")), FileManager())

        manager.state.test {
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    @Ignore
    fun `should sync image on invocation`() = runTest {
        val manager = SyncManager(inMemoryHttpClient(), FileManager())

        manager.state.test {
            assertEquals(emptyMap(), awaitItem())

            println("=== Syncing random image...")
            manager.sync(Image(RandomImage, Path("$RandomImage")))

            assertEquals(mapOf(RandomImage to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    @Ignore
    fun `should put synced image without content`() = runTest {
        val manager = SyncManager(inMemoryHttpClient(listOf("$RandomImage")), FileManager())

        manager.state.test {
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())

            manager.sync(Image(RandomImage, Path("$RandomImage")))

            assertEquals(mapOf(RandomImage to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    @Ignore
    fun `should include content length header`() = runTest {
        val manager = SyncManager(inMemoryHttpClient(), FileManager())

        manager.state.test {
            manager.sync(Image(RandomImage, Path("$RandomImage")))

            cancelAndIgnoreRemainingEvents()
        }
    }
}

private fun inMemoryHttpClient(initialValue: List<String> = emptyList()) = HttpClient(
    engine = inMemoryHttpClientEngine(initialValue),
    block = DefaultHttpConfiguration,
)
