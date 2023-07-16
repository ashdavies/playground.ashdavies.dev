package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.net.URI

internal actual fun PathProvider(context: Context): PathProvider = object : PathProvider {
    override val images: URI get() = throw UnsupportedOperationException()
}

@Composable
internal actual fun rememberPathProvider(): PathProvider = remember {
    PathProvider(Context.Default)
}
