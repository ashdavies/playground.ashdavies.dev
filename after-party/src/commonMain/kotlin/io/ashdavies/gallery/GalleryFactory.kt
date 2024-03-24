package io.ashdavies.gallery

import androidx.compose.runtime.remember
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.identity.IdentityModule

public fun galleryPresenterFactory(context: PlatformContext): Presenter.Factory {
    val playgroundDatabase = GalleryModule.playgroundDatabase(context)

    return presenterFactoryOf<GalleryScreen> { _, _ ->
        GalleryPresenter(
            imageManager = remember(playgroundDatabase) {
                GalleryModule.imageManager(context, playgroundDatabase.imageQueries)
            },
            syncManager = remember {
                GalleryModule.syncManager()
            },
        )
    }
}

public fun galleryUiFactory(context: PlatformContext): Ui.Factory {
    val storageManager = StorageManager(PathProvider(context))

    return uiFactoryOf<GalleryScreen, GalleryScreen.State> { _, state, modifier ->
        GalleryScreen(state, storageManager, modifier)
        context.reportFullyDrawn()
    }
}
