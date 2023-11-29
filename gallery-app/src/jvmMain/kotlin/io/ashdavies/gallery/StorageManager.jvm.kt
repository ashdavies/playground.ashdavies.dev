package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

internal actual fun StorageManager(
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext,
): StorageManager = object : StorageManager {

    override suspend fun create(platformContext: PlatformContext): File = withContext(coroutineContext) {
        File(pathProvider.getImagesPath(), "${randomUuid()}.jpg").apply {
            if (!createNewFile()) throw IllegalStateException()
        }
    }

    override fun delete(file: File): Boolean {
        return file.delete()
    }
}
