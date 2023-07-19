package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File

internal actual fun PathProvider(context: Context): PathProvider = object : PathProvider {
    override val images: File = File(context.filesDir, "images").apply(File::mkdirs)
}

@Composable
internal actual fun rememberPathProvider(): PathProvider {
    return rememberPathProvider(LocalContext.current)
}

@Composable
internal fun rememberPathProvider(context: Context) = remember {
    PathProvider(context)
}
