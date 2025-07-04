package io.ashdavies.tally.gallery

import app.cash.sqldelight.coroutines.mapToList
import io.ashdavies.sql.Suspended
import io.ashdavies.sql.mapAsFlow
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal interface ImageManager {
    val list: Flow<List<Image>>
    suspend fun add(file: File)
    suspend fun remove(image: Image)
}

internal fun ImageManager(
    storageManager: StorageManager,
    imageQueries: Suspended<ImageQueries>,
    coroutineContext: CoroutineContext,
): ImageManager = object : ImageManager {

    override val list = imageQueries
        .mapAsFlow { it.selectAll() }
        .mapToList(coroutineContext)

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun add(file: File) {
        val image = Image(
            uuid = "${Uuid.random()}",
            name = file.getName(),
            path = file.getAbsolutePath(),
        )

        imageQueries().insertOrReplace(image)
    }

    override suspend fun remove(image: Image) {
        storageManager.delete(File(image.path))
        imageQueries().deleteById(image.uuid)
    }
}
