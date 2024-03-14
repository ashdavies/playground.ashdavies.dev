package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.party.PlaygroundDatabase
import io.ashdavies.sql.DatabaseFactory

internal object GalleryModule {
    fun playgroundDatabase(context: PlatformContext): PlaygroundDatabase = DatabaseFactory(
        schema = PlaygroundDatabase.Schema,
        context = context,
        factory = { PlaygroundDatabase(it) },
    )

    fun imageManager(context: PlatformContext, imageQueries: ImageQueries): ImageManager = ImageManager(
        storage = StorageManager(PathProvider(context)),
        queries = imageQueries,
    )

    fun syncManager(initialValue: List<String> = emptyList()): SyncManager = SyncManager(
        client = defaultHttpClient(inMemoryHttpClientEngine(initialValue)),
        reader = File::readChannel,
    )
}
