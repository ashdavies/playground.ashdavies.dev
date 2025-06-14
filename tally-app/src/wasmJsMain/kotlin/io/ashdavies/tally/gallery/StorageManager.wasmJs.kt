package io.ashdavies.tally.gallery

import io.ashdavies.content.PlatformContext
import kotlin.coroutines.CoroutineContext

internal actual fun StorageManager(
    platformContext: PlatformContext,
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext,
): StorageManager = TODO()
