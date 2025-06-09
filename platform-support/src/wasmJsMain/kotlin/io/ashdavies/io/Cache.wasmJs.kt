package io.ashdavies.io

import io.ashdavies.content.PlatformContext
import okio.Path
import okio.FileSystem

internal enum class OS {
    Linux,
    Windows,
    MacOS,
}

public actual fun PlatformContext.resolveCacheDir(relative: String): Path {
    return FileSystem.SYSTEM_TEMPORARY_DIRECTORY
}
