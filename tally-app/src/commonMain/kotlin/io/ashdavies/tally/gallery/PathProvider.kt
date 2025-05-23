package io.ashdavies.tally.gallery

import io.ashdavies.content.PlatformContext

internal fun interface PathProvider {
    suspend fun getImagesPath(): File
}

internal expect fun PathProvider(
    platformContext: PlatformContext,
): PathProvider
