package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.io.File

internal actual fun StorageProvider(context: Context, paths: FileProvider): StorageProvider = object : StorageProvider {
    override val images: Flow<List<Uri>> = emptyFlow()
}

internal actual fun StorageProvider.delete(target: Uri): Boolean = with(File(target.path)) {
    if (exists()) delete() else throw IllegalArgumentException("Path '$target' does not exist")
}

@Composable
internal actual fun rememberStorageProvider(paths: FileProvider): StorageProvider = remember(paths) {
    StorageProvider(Context.Default, paths)
}
