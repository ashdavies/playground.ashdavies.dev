package io.ashdavies.tally.gallery

import io.ashdavies.content.PlatformContext

internal actual fun PathProvider(
    platformContext: PlatformContext,
): PathProvider = object : PathProvider {
    override suspend fun getImagesPath(): File {
        // In browser environment, we'll use a virtual path for images
        return File("/images")
    }
}
