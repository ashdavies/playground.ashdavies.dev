package io.ashdavies.tally.gallery

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.sql.map
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface GalleryModule {

    @IntoSet
    @Provides
    fun galleryPresenterFactory(
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
        remoteAnalytics: RemoteAnalytics,
    ): Presenter.Factory = presenterFactoryOf<GalleryScreen, GalleryScreen.State> { _, _ ->
        GalleryPresenter(
            imageManager = ImageManager(
                imageQueries = databaseFactory.map { it.imageQueries },
                coroutineContext = Dispatchers.Default,
            ),
            syncManager = SyncManager(
                client = HttpClient(
                    engine = inMemoryHttpClientEngine(),
                    block = DefaultHttpConfiguration,
                ),
            ),
            remoteAnalytics = remoteAnalytics,
        )
    }

    @IntoSet
    @Provides
    fun galleryUiFactory(): Ui.Factory = uiFactoryOf<GalleryScreen, GalleryScreen.State> { state, modifier ->
        GalleryScreen(
            state = state,
            modifier = modifier,
        )
    }
}
