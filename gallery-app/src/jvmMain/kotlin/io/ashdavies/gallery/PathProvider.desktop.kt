package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext

private val codeSourceLocation = PathProvider::class.java.protectionDomain.codeSource.location

internal actual fun PathProvider(context: PlatformContext): PathProvider = object : PathProvider {
    override val images: File = File(codeSourceLocation.toURI())
}
