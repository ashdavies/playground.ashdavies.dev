package io.ashdavies.gallery

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random.Default.nextLong

internal interface SyncManager {
    fun state(): Flow<Map<String, SyncState>>
    suspend fun sync(image: Image)
}

internal enum class SyncState {
    NOT_SYNCED,
    SYNCING,
    SYNCED,
}

internal fun SyncManager(): SyncManager {
    return InMemorySyncManager()
}

private class InMemorySyncManager : SyncManager {

    private val state = MutableStateFlow<Map<String, SyncState>>(emptyMap())

    override fun state(): Flow<Map<String, SyncState>> = state.asStateFlow()

    override suspend fun sync(image: Image) {
        state.update { it + (image.name to SyncState.SYNCING) }
        delay(5_400L + nextLong(5_400))

        state.update { it + (image.name to SyncState.SYNCED) }
    }
}
