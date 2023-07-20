package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

internal actual fun PathProvider(context: Context): PathProvider = object : PathProvider {
    override val images: File = File(PathProvider::class.java.protectionDomain.codeSource.location.toURI())
}

@Composable
internal actual fun rememberPathProvider(): PathProvider = remember {
    PathProvider(Context.Default)
}
