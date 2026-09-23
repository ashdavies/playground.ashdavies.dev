package dev.ashdavies.playground.gallery

import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

public interface SyncManager {
    public val state: Flow<Map<Uuid, SyncState>>
    public suspend fun sync(image: Image)
}

public enum class SyncState {
    NOT_SYNCED,
    SYNCING,
    SYNCED,
}
