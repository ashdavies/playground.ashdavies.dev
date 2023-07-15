package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

internal interface StorageProvider {
    val images: Flow<List<Uri>>
}

internal expect fun StorageProvider(
    context: Context,
    paths: FileProvider,
): StorageProvider

internal expect fun StorageProvider.delete(
    target: Uri,
): Boolean

@Composable
internal expect fun rememberStorageProvider(
    paths: FileProvider = rememberFileProvider(),
): StorageProvider
