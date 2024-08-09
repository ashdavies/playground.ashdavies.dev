package io.ashdavies.party.gallery

import io.ashdavies.content.PlatformContext
import io.ashdavies.party.gallery.PathProvider
import java.io.File

internal actual fun PathProvider(context: PlatformContext): PathProvider = PathProvider {
    File(context.filesDir, "images").apply(File::mkdirs)
}
