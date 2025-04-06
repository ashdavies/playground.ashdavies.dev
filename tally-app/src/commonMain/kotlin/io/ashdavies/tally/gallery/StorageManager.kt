package io.ashdavies.tally.gallery

import io.ashdavies.content.PlatformContext
import kotlin.coroutines.CoroutineContext

internal interface StorageManager {
    suspend fun create(): File
    suspend fun delete(file: File): Boolean
}

internal expect fun StorageManager(
    platformContext: PlatformContext,
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext,
): StorageManager
