package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File

internal actual fun FileProvider(context: Context): FileProvider = object : FileProvider {
    override val images: Uri = Uri
        .create("file://${context.filesDir}/images")
        .also { File(it).mkdirs() }
}

@Composable
internal actual fun rememberFileProvider(): FileProvider {
    return rememberPathProvider(LocalContext.current)
}

@Composable
internal fun rememberPathProvider(context: Context) = remember {
    FileProvider(context)
}
