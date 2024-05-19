package io.ashdavies.playground

import io.ashdavies.content.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

internal actual fun PlatformContext.resolveCacheDir(relative: String): Path = cacheDir
    .resolve(relative)
    .toOkioPath()
