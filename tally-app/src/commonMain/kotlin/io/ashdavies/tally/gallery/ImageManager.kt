package io.ashdavies.tally.gallery

import app.cash.sqldelight.coroutines.mapToList
import io.ashdavies.sql.Suspended
import io.ashdavies.sql.mapAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.io.files.Path
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

internal interface ImageManager {
    val list: Flow<List<Image>>
    suspend fun add(path: Path): Image
    suspend fun remove(image: Image)
}

internal fun ImageManager(
    imageQueries: Suspended<ImageQueries>,
    coroutineContext: CoroutineContext,
): ImageManager = object : ImageManager {

    override val list = imageQueries
        .mapAsFlow { it.selectAll() }
        .mapToList(coroutineContext)

    override suspend fun add(path: Path) = Image(Uuid.random(), path).also {
        imageQueries().insertOrReplace(it)
    }

    override suspend fun remove(image: Image) {
        imageQueries().deleteById(image.uuid)
    }
}
