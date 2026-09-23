package dev.ashdavies.playground.gallery

import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.metro.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SuspendLazy
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
public interface GalleryModule {

    @Provides
    @ExperimentalMetroCoroutinesApi
    public fun imageManager(playgroundDatabase: SuspendLazy<PlaygroundDatabase>): ImageManager = ImageManager(
        imageQueries = playgroundDatabase.map { it.imageQueries },
        fileManager = FileManager(),
        coroutineContext = Dispatchers.Default,
    )

    @Provides
    @ExperimentalMetroCoroutinesApi
    public fun syncManager(localGallery: SuspendLazy<LocalGallery>): SyncManager = DeferredSyncManager {
        if (localGallery.await().enabled) {
            LocalSyncManager()
        } else {
            RemoteSyncManager(
                httpClient = defaultHttpClient { },
                fileManager = FileManager(),
            )
        }
    }
}
