package io.ashdavies.gallery

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

internal actual fun StorageProvider(context: Context, paths: FileProvider): StorageProvider = object : StorageProvider {
    override val images: Flow<List<Uri>> = callbackFlow {
        val result = observeFileDirectory(context, paths.images) { sendOrThrow(listOf(Uri.create(it))) }
        val files = File(paths.images).listFiles() ?: emptyArray()
        sendOrThrow(files.map { it.toURI() })
        awaitClose { result.cancel() }
    }
}

internal actual fun StorageProvider.delete(target: Uri): Boolean = with(File(target.path)) {
    if (exists()) delete() else throw IllegalArgumentException("Path '$target' does not exist")
}

@Composable
internal actual fun rememberStorageProvider(paths: FileProvider): StorageProvider {
    val context = LocalContext.current
    return remember(paths) {
        StorageProvider(context, paths)
    }
}

private fun <T : Any> ProducerScope<T>.sendOrThrow(element: T) {
    trySend(element).onFailure { if (it != null) throw it }
}
