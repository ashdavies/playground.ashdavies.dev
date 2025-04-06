package io.ashdavies.tally.gallery

import io.ashdavies.content.PlatformContext
import java.io.File

internal actual fun PathProvider(
    platformContext: PlatformContext,
): PathProvider = PathProvider {
    File(
        /* parent = */ platformContext.filesDir,
        /* child = */ "images",
    ).apply(File::mkdirs)
}
