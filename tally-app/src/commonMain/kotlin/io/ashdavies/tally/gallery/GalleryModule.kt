package io.ashdavies.tally.gallery

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.sql.map
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ashdavies.tally.files.FileManager
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

    @IntoSet
    @Provides
    fun galleryPresenterFactory(provider: Provider<GalleryPresenter>): Presenter.Factory {
        return presenterFactoryOf<GalleryScreen, _>(provider)
    }

    @IntoSet
    @Provides
    fun galleryUiFactory(provider: Provider<GalleryUi>): Ui.Factory {
        return uiFactoryOf<GalleryScreen, _>(provider)
    }
}
