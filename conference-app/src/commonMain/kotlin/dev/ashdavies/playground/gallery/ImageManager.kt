package dev.ashdavies.playground.gallery

import app.cash.sqldelight.coroutines.mapToList
import dev.ashdavies.playground.files.FileManager
import dev.ashdavies.playground.files.Path
import dev.ashdavies.sql.Suspended
import dev.ashdavies.sql.mapAsFlow
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

internal interface ImageManager {
    val list: Flow<List<Image>>
    suspend fun add(path: Path): Image
    suspend fun remove(image: Image)
}

internal fun ImageManager(
    imageQueries: Suspended<ImageQueries>,
    fileManager: FileManager,
    coroutineContext: CoroutineContext,
): ImageManager = object : ImageManager {

    override val list = imageQueries
        .mapAsFlow { it.selectAll() }
        .mapToList(coroutineContext)

    override suspend fun add(path: Path) = Image(Uuid.random(), path).also {
        imageQueries().insertOrReplace(it)
    }

    override suspend fun remove(image: Image) {
        check(imageQueries().deleteById(image.uuid) > 0)
        fileManager.delete(image.path)
    }
}
