package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun getFilesDir(): String {
    return LocalContext.current.filesDir.absolutePath
}
