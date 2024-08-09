package io.ashdavies.party.gallery

import androidx.compose.runtime.remember
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn

public fun galleryPresenterFactory(context: PlatformContext): Presenter.Factory {
    return presenterFactoryOf<GalleryScreen> { _, _ -> GalleryPresenter(context) }
}

public fun galleryUiFactory(context: PlatformContext): Ui.Factory {
    return uiFactoryOf<GalleryScreen, GalleryScreen.State> { _, state, modifier ->
        GalleryScreen(state, remember { StorageManager(PathProvider(context)) }, modifier)
        context.reportFullyDrawn()
    }
}
