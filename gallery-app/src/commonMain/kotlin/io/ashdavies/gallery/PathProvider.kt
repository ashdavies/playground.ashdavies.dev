package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import io.ashdavies.content.PlatformContext

internal interface PathProvider {
    val images: File
}

internal expect fun PathProvider(
    context: PlatformContext,
): PathProvider

@Composable
internal expect fun rememberPathProvider(): PathProvider
