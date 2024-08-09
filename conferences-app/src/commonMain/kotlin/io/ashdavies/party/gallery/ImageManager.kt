package io.ashdavies.party.gallery

import io.ashdavies.content.PlatformContext
import io.ashdavies.gallery.Image
import io.ashdavies.gallery.ImageQueries
import io.ashdavies.sql.mapToList
import io.ashdavies.util.randomUuid
import kotlinx.coroutines.flow.Flow

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

    override fun add(file: File) {
        val image = Image(
            uuid = randomUuid(),
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
