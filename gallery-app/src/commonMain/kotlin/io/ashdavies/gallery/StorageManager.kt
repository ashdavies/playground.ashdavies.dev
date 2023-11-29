package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext
import kotlin.coroutines.CoroutineContext

internal interface StorageManager {
    suspend fun create(platformContext: PlatformContext): File
    fun delete(file: File): Boolean
}

internal expect fun StorageManager(
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext,
): StorageManager
