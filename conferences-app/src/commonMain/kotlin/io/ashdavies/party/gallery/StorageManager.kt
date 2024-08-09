package io.ashdavies.party.gallery

import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal interface StorageManager {
    suspend fun create(platformContext: PlatformContext): File
    suspend fun delete(file: File): Boolean
}

internal expect fun StorageManager(
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext = Dispatchers.IO,
): StorageManager
