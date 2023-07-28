package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext
import java.io.File

internal actual fun PathProvider(context: PlatformContext): PathProvider = object : PathProvider {
    override val images: File = File(context.filesDir, "images").apply(File::mkdirs)
}
