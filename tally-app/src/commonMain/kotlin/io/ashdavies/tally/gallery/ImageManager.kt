package io.ashdavies.tally.gallery

import io.ashdavies.sql.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal interface ImageManager {
    val list: Flow<List<Image>>
    suspend fun add(file: File)
    suspend fun remove(image: Image)
}

internal fun ImageManager(
    storageManager: StorageManager,
    imageQueries: ImageQueries,
): ImageManager = object : ImageManager {

    override val list = imageQueries
        .selectAll()
        .mapToList(Dispatchers.Default)

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun add(file: File) {
        val image = Image(
            uuid = "${Uuid.random()}",
            name = file.getName(),
            path = file.getAbsolutePath(),
        )

        imageQueries.insertOrReplace(image)
    }

    override suspend fun remove(image: Image) {
        storageManager.delete(File(image.path))
        imageQueries.deleteById(image.uuid)
    }
}
