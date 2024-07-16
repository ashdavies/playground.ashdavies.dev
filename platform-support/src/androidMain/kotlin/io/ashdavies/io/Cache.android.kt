package io.ashdavies.io

import io.ashdavies.content.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

public actual fun PlatformContext.resolveCacheDir(relative: String): Path = cacheDir
    .resolve(relative)
    .toOkioPath()
