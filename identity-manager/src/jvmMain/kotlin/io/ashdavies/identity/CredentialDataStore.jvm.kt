package io.ashdavies.identity

import androidx.datastore.core.DataStore
import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public actual val PlatformContext.credentialDataStore: DataStore<Credential>
    get() = EmptyDataStore()

private class EmptyDataStore<T>(override val data: Flow<T> = emptyFlow()) : DataStore<T> {
    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        TODO("Not yet implemented")
    }
}
