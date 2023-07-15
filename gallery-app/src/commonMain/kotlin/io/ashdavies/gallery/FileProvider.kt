package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import java.net.URI

internal interface FileProvider {
    val images: URI
}

internal expect fun FileProvider(
    context: Context,
): FileProvider

@Composable
internal expect fun rememberFileProvider(): FileProvider
