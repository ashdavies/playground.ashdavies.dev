package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext
import java.io.File

internal actual fun PathProvider(context: PlatformContext): PathProvider = PathProvider {
    File(context.filesDir, "images").apply(File::mkdirs)
}
