package io.ashdavies.tally.upcoming

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
internal interface UpcomingModule {

    @IntoSet
    @Provides
    fun upcomingPresenterFactory(provider: Provider<UpcomingPresenter>): Presenter.Factory {
        return presenterFactoryOf<UpcomingScreen, _>(provider)
    }

    @IntoSet
    @Provides
    fun upcomingUiFactory(provider: Provider<UpcomingUi>): Ui.Factory {
        return uiFactoryOf<UpcomingScreen, _>(provider)
    }
}
