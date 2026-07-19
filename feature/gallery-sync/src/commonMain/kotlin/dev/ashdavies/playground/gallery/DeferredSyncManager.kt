package dev.ashdavies.playground.gallery

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.uuid.Uuid

internal class DeferredSyncManager(private val factory: suspend () -> SyncManager) : SyncManager {

    private val deferred = CompletableDeferred<SyncManager>()

    override val state: Flow<Map<Uuid, SyncState>>
        field = MutableStateFlow(emptyMap())

    override suspend fun sync(image: Image) {
        val delegate = if (deferred.isCompleted) {
            deferred.getCompleted()
        } else {
            deferred.complete(factory())
            deferred.await()
        }

        delegate.sync(image)
    }
}
