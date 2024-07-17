package io.ashdavies.io

import io.ashdavies.content.PlatformContext
import okio.Path

private const val DEFAULT_CACHE_PATH = "build/cache"

public expect fun PlatformContext.resolveCacheDir(
    relative: String = DEFAULT_CACHE_PATH,
): Path
