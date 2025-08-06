package dev.ashdavies.tally.gallery

import dev.ashdavies.http.DefaultHttpConfiguration
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.ashdavies.tally.PlaygroundDatabase
import dev.ashdavies.tally.files.FileManager
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface GalleryModule {

    @Provides
    fun imageManager(databaseFactory: DatabaseFactory<PlaygroundDatabase>): ImageManager = ImageManager(
        imageQueries = databaseFactory.map { it.imageQueries },
        fileManager = FileManager(),
        coroutineContext = Dispatchers.Default,
    )

    @Provides
    fun syncManager(): SyncManager = SyncManager(
        httpClient = HttpClient(
            engine = inMemoryHttpClientEngine(),
            block = DefaultHttpConfiguration,
        ),
        fileManager = FileManager(),
    )
}
