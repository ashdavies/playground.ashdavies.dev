package io.ashdavies.tally.gallery

import io.ashdavies.content.PlatformContext

private val codeSourceLocation = PathProvider::class.java.protectionDomain.codeSource.location

internal actual fun PathProvider(
    platformContext: PlatformContext,
): PathProvider = PathProvider {
    File(codeSourceLocation.toURI())
}
