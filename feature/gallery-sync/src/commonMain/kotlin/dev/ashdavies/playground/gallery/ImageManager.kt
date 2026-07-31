package dev.ashdavies.playground.gallery

import app.cash.sqldelight.coroutines.mapToList
import dev.ashdavies.playground.metro.mapAsFlow
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.SuspendLazy
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

public interface ImageManager {
    public val list: Flow<List<Image>>
    public suspend fun add(path: Path): Image
    public suspend fun remove(image: Image)
}

@ExperimentalMetroCoroutinesApi
internal fun ImageManager(
    imageQueries: SuspendLazy<ImageQueries>,
    fileManager: FileManager,
    coroutineContext: CoroutineContext,
): ImageManager = object : ImageManager {

    override val list = imageQueries
        .mapAsFlow { it.selectAll() }
        .mapToList(coroutineContext)

    override suspend fun add(path: Path) = Image(Uuid.random(), path).also {
        imageQueries.await().insertOrReplace(it)
    }

    override suspend fun remove(image: Image) {
        check(imageQueries.await().deleteById(image.uuid) > 0)
        fileManager.delete(image.path)
    }
}
