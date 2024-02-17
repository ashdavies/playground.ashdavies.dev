package io.ashdavies.gallery

import androidx.compose.runtime.remember
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.identity.IdentityModule

public fun GalleryPresenterFactory(context: PlatformContext): Presenter.Factory {
    val playgroundDatabase = GalleryModule.playgroundDatabase(context)

    return presenterFactoryOf<GalleryScreen> { _, _ ->
        GalleryPresenter(
            identityManager = remember(playgroundDatabase) {
                IdentityModule.identityManager(context, playgroundDatabase.credentialQueries)
            },
            imageManager = remember(playgroundDatabase) {
                GalleryModule.imageManager(context, playgroundDatabase.imageQueries)
            },
            syncManager = remember {
                GalleryModule.syncManager()
            },
        )
    }
}

public fun GalleryUiFactory(context: PlatformContext): Ui.Factory {
    val storageManager = StorageManager(PathProvider(context))

    return uiFactoryOf<GalleryScreen, GalleryScreen.State> { _, state, modifier ->
        GalleryScreen(state, storageManager, modifier)
        context.reportFullyDrawn()
    }
}
