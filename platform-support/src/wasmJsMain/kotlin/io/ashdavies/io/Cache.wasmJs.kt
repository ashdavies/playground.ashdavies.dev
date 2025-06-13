package io.ashdavies.io

import io.ashdavies.content.PlatformContext
import okio.FileSystem
import okio.Path

public actual fun PlatformContext.resolveCacheDir(relative: String): Path {
    return FileSystem.SYSTEM_TEMPORARY_DIRECTORY
}
