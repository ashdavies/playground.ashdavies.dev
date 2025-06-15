package io.ashdavies.tally.gallery

import io.ashdavies.content.PlatformContext
import kotlin.coroutines.CoroutineContext

internal actual fun StorageManager(
    platformContext: PlatformContext,
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext,
): StorageManager = object : StorageManager {
    override suspend fun create(): File = TODO()
    override suspend fun delete(file: File): Boolean = TODO()
}
