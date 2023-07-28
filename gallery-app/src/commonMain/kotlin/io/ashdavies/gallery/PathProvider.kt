package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext

internal interface PathProvider {
    val images: File
}

internal expect fun PathProvider(
    context: PlatformContext,
): PathProvider
