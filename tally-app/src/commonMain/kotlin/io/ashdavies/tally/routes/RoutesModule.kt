package io.ashdavies.tally.routes

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ashdavies.content.PlatformContext
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ktor.client.HttpClient

@ContributesTo(AppScope::class)
internal interface RoutesModule {

    @Provides
    @SingleIn(AppScope::class)
    fun locationService(context: PlatformContext): LocationService = LocationService(context)

    @IntoSet
    @Provides
    fun routesPresenterFactory(
        locationService: LocationService,
        httpClient: HttpClient,
    ): Presenter.Factory = presenterFactoryOf<RoutesScreen, RoutesScreen.State> { screen, _ ->
        RoutesPresenter(
            locationService = locationService,
            httpClient = httpClient,
        )
    }

    @IntoSet
    @Provides
    fun routesUiFactory(): Ui.Factory = uiFactoryOf<RoutesScreen, RoutesScreen.State> { state, modifier ->
        RoutesScreen(
            state = state,
            modifier = modifier,
        )
    }
}
