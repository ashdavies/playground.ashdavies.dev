package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

internal interface StorageManager {
    fun create(context: Context): File
    fun list(): List<File>
    fun delete(file: File): Boolean
}

internal expect fun StorageManager(
    parent: File,
): StorageManager

@Composable
internal fun rememberStorageManager(
    paths: PathProvider = rememberPathProvider(),
): StorageManager = remember(paths) {
    StorageManager(paths.images)
}
