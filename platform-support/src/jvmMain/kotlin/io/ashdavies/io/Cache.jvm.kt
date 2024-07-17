package io.ashdavies.io

import io.ashdavies.content.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File

internal enum class OS {
    Linux,
    Windows,
    MacOS,
}

private val currentOS: OS by lazy {
    val os = System.getProperty("os.name").lowercase()

    when {
        os.equals("Mac OS X", ignoreCase = true) -> OS.MacOS
        os.startsWith("Win", ignoreCase = true) -> OS.Windows
        os.startsWith("Linux", ignoreCase = true) -> OS.Linux
        else -> error("Unknown OS name: $os")
    }
}

public actual fun PlatformContext.resolveCacheDir(relative: String): Path {
    val cacheDir = when (currentOS) {
        OS.MacOS -> File(System.getProperty("user.home"), "Library/Caches/playground")
        OS.Linux -> File(System.getProperty("user.home"), ".cache/playground")
        OS.Windows -> File(System.getenv("AppData"), "playground/cache")
    }

    return cacheDir
        .resolve(relative)
        .toOkioPath()
}
