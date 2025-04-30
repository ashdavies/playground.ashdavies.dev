package io.ashdavies.routes

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient

@ContributesTo(AppScope::class)
internal interface RoutesFactoryModule {

    @IntoSet
    @Provides
    fun routesPresenterFactory(
        locationService: LocationService,
        httpClient: HttpClient,
    ): Presenter.Factory = Presenter.Factory { screen, _, _ ->
        when (screen) {
            is RoutesScreen -> presenterOf {
                RoutesPresenter(
                    locationService = locationService,
                    httpClient = httpClient,
                )
            }

            else -> null
        }
    }

    @IntoSet
    @Provides
    fun routesUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
        when (screen) {
            is RoutesScreen -> ui<RoutesScreen.State> { state, modifier ->
                RoutesScreen(state, modifier)
            }

            else -> null
        }
    }
}
