package io.ashdavies.tally.routes

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
internal interface RoutesModule {

    @IntoSet
    @Provides
    fun routesPresenterFactory(provider: Provider<RoutesPresenter>): Presenter.Factory {
        return presenterFactoryOf<RoutesScreen, _>(provider)
    }

    @IntoSet
    @Provides
    fun routesUiFactory(provider: Provider<RoutesUi>): Ui.Factory {
        return uiFactoryOf<RoutesScreen, _>(provider)
    }
}
