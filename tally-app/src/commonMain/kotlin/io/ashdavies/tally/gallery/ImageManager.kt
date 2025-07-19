package io.ashdavies.tally.gallery

import app.cash.sqldelight.coroutines.mapToList
import io.ashdavies.sql.Suspended
import io.ashdavies.sql.mapAsFlow
import io.ashdavies.tally.files.FileManager
import io.ashdavies.tally.files.Path
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
