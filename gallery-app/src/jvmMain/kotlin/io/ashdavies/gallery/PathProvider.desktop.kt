package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.net.URI

internal actual fun FileProvider(context: Context): FileProvider = object : FileProvider {
    override val images: URI get() = throw UnsupportedOperationException()
}

@Composable
internal actual fun rememberFileProvider(): FileProvider = remember {
    FileProvider(Context.Default)
}
