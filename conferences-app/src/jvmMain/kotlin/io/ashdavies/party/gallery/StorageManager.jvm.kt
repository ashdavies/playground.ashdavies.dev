package io.ashdavies.party.gallery

import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal actual fun StorageManager(
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext,
): StorageManager = object : StorageManager {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun create(platformContext: PlatformContext): File = withContext(coroutineContext) {
        File(pathProvider.getImagesPath(), "${Uuid.random()}.jpg").apply {
            require(createNewFile()) { "File already exists" }
        }
    }

    override suspend fun delete(file: File): Boolean = withContext(coroutineContext) {
        file.delete()
    }
}
