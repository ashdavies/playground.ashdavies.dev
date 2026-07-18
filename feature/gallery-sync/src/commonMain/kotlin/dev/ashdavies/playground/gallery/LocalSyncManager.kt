package dev.ashdavies.playground.gallery

import dev.zacsweers.metro.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@Inject
internal class LocalSyncManager : SyncManager {

    override val state: Flow<Map<Uuid, SyncState>>
        field = MutableStateFlow(emptyMap())

    override suspend fun sync(image: Image) {
        state.update { it + (image.uuid to SyncState.SYNCING) }
        delay(1.seconds)

        state.update { it + (image.uuid to SyncState.SYNCED) }
    }
}
