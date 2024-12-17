package io.ashdavies.party.gallery

import io.ashdavies.content.PlatformContext
import io.ashdavies.sql.mapToList
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal interface ImageManager {
    val list: Flow<List<Image>>
    fun add(file: File)
    suspend fun remove(image: Image)
}

internal fun ImageManager(
    context: PlatformContext,
    queries: ImageQueries,
): ImageManager = ImageManager(
    storage = StorageManager(PathProvider(context)),
    queries = queries,
)

internal fun ImageManager(
    storage: StorageManager,
    queries: ImageQueries,
): ImageManager = object : ImageManager {

    override val list: Flow<List<Image>> = queries
        .selectAll()
        .mapToList()

    @OptIn(ExperimentalUuidApi::class)
    override fun add(file: File) {
        val image = Image(
            uuid = "${Uuid.random()}",
            name = file.getName(),
            path = file.getAbsolutePath(),
        )

        queries.insertOrReplace(image)
    }

    override suspend fun remove(image: Image) {
        storage.delete(File(image.path))
        queries.deleteById(image.uuid)
    }
}
