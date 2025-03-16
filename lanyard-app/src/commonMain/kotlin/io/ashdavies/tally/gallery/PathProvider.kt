package io.ashdavies.lanyard.gallery

import io.ashdavies.content.PlatformContext

internal fun interface PathProvider {
    suspend fun getImagesPath(): File
}

internal expect fun PathProvider(
    context: PlatformContext,
): PathProvider
