package io.ashdavies.gallery

import app.cash.turbine.test
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.tally.gallery.Image
import io.ashdavies.tally.gallery.SyncManager
import io.ashdavies.tally.gallery.SyncState
import io.ashdavies.tally.gallery.inMemoryHttpClientEngine
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.Uuid

private val RandomImage = Uuid.random()

internal class SyncManagerTest {

    @Test
    fun `should request initial value`() = runTest {
        val manager = SyncManager(inMemoryHttpClient(listOf("$RandomImage")))

        manager.state.test {
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should sync image on invocation`() = runTest {
        val manager = SyncManager(inMemoryHttpClient())

        manager.state.test {
            assertEquals(emptyMap(), awaitItem())

            SystemFileSystem.createTempFile("$RandomImage") { randomImagePath ->
                manager.sync(Image(RandomImage, randomImagePath))
            }

            assertEquals(mapOf(RandomImage to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should put synced image without content`() = runTest {
        val manager = SyncManager(inMemoryHttpClient(listOf("$RandomImage")))

        manager.state.test {
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())

            SystemFileSystem.createTempFile("$RandomImage") { randomImagePath ->
                manager.sync(Image(RandomImage, randomImagePath))
            }

            assertEquals(mapOf(RandomImage to SyncState.SYNCING), awaitItem())
            assertEquals(mapOf(RandomImage to SyncState.SYNCED), awaitItem())
        }
    }

    @Test
    fun `should include content length header`() = runTest {
        val manager = SyncManager(inMemoryHttpClient())

        manager.state.test {
            SystemFileSystem.createTempFile("$RandomImage") { randomImagePath ->
                manager.sync(Image(RandomImage, randomImagePath))
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `temp file deleted after test execution`() = runTest {
        lateinit var tempFilePath: Path

        SystemFileSystem.createTempFile("$RandomImage") { randomImagePath ->
            assertTrue(exists(randomImagePath))
            tempFilePath = randomImagePath
        }

        assertFalse(SystemFileSystem.exists(tempFilePath))
    }
}

private fun inMemoryHttpClient(initialValue: List<String> = emptyList()) = HttpClient(
    engine = inMemoryHttpClientEngine(initialValue),
    block = DefaultHttpConfiguration,
)

private inline fun FileSystem.createTempFile(prefix: String, action: FileSystem.(Path) -> Unit) {
    Path(SystemTemporaryDirectory, prefix).also { tempFilePath ->
        sink(tempFilePath)
        action(tempFilePath)
        delete(tempFilePath)
    }
}
