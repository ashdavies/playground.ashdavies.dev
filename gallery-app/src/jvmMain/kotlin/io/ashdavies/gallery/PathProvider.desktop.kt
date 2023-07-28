package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.content.PlatformContext

private val codeSourceLocation = PathProvider::class.java.protectionDomain.codeSource.location

internal actual fun PathProvider(context: PlatformContext): PathProvider = object : PathProvider {
    override val images: File = File(codeSourceLocation.toURI())
}

@Composable
internal actual fun rememberPathProvider(): PathProvider = remember {
    PathProvider(PlatformContext.Default)
}
