package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

internal actual fun PathProvider(context: Context): PathProvider = object : PathProvider {
    override val images: File get() = throw UnsupportedOperationException()
}

@Composable
internal actual fun rememberPathProvider(): PathProvider = remember {
    PathProvider(Context.Default)
}
