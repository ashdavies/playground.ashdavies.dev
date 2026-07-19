package dev.ashdavies.playground.gallery

import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
public interface GalleryModule {

    @Provides
    public fun imageManager(databaseFactory: DatabaseFactory<PlaygroundDatabase>): ImageManager = ImageManager(
        imageQueries = databaseFactory.map { it.imageQueries },
        fileManager = FileManager(),
        coroutineContext = Dispatchers.Default,
    )

    @Provides
    public fun syncManager(localGallery: LocalGallery): SyncManager = DeferredSyncManager {
        if (localGallery.enabled()) {
            LocalSyncManager()
        } else {
            RemoteSyncManager(
                httpClient = defaultHttpClient { },
                fileManager = FileManager(),
            )
        }
    }
}
