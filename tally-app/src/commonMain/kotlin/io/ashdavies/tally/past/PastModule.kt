package io.ashdavies.tally.past

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf

@ContributesTo(AppScope::class)
internal interface PastModule {

    @IntoSet
    @Provides
    fun pastPresenterFactory(provider: Provider<PastPresenter>): Presenter.Factory {
        return presenterFactoryOf<PastScreen, _>(provider)
    }

    @IntoSet
    @Provides
    fun pastUiFactory(provider: Provider<PastUi>): Ui.Factory {
        return uiFactoryOf<PastScreen, _>(provider)
    }
}
