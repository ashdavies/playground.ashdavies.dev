package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.mapToList
import io.ashdavies.playground.rememberDatabase
import kotlinx.coroutines.flow.Flow

internal interface ImageManager {
    fun list(): Flow<List<Image>>
    fun add(file: File)
    fun remove(image: Image)
}

internal fun ImageManager(
    storage: StorageManager,
    queries: ImageQueries,
): ImageManager = object : ImageManager {

    override fun list(): Flow<List<Image>> {
        return queries.selectAll().mapToList()
    }

    override fun add(file: File) {
        val image = Image(
            uuid = randomUuid(),
            name = file.getName(),
            path = file.getAbsolutePath(),
        )

        queries.insertOrReplace(image)
    }

    override fun remove(image: Image) {
        storage.delete(File(image.path))
        queries.deleteById(image.uuid)
    }
}

@Composable
internal fun rememberImageManager(
    storage: StorageManager = rememberStorageManager(),
    queries: ImageQueries = rememberImageQueries(),
): ImageManager = remember(storage, queries) {
    ImageManager(storage, queries)
}

@Composable
private fun rememberImageQueries(
    database: PlaygroundDatabase = rememberDatabase(
        factory = PlaygroundDatabase::invoke,
        schema = PlaygroundDatabase.Schema,
    ),
): ImageQueries = remember(database) {
    database.imageQueries
}
