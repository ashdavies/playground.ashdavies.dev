package io.ashdavies.tally.gallery

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.http.DefaultHttpConfiguration
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ktor.client.HttpClient

internal object GalleryModule {

    fun galleryPresenterFactory(
        storageManager: StorageManager,
        imageQueries: ImageQueries,
        remoteAnalytics: RemoteAnalytics,
    ): Presenter.Factory = presenterFactoryOf<GalleryScreen, GalleryScreen.State> { _, _ ->
        GalleryPresenter(
            imageManager = ImageManager(
                storageManager = storageManager,
                imageQueries = imageQueries,
            ),
            syncManager = SyncManager(
                client = HttpClient(
                    engine = inMemoryHttpClientEngine(),
                    block = DefaultHttpConfiguration,
                ),
                reader = File::readChannel,
            ),
            remoteAnalytics = remoteAnalytics,
        )
    }

    fun galleryUiFactory(
        storageManager: StorageManager,
    ): Ui.Factory = uiFactoryOf<GalleryScreen, GalleryScreen.State> { state, modifier ->
        GalleryScreen(
            state = state,
            manager = storageManager,
            modifier = modifier,
        )
    }
}
