package io.ashdavies.tally.home

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.identity.CredentialQueries
import io.ashdavies.identity.IdentityManager
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf

internal object HomeModule {

    fun homePresenterFactory(
        platformContext: PlatformContext,
        credentialQueries: CredentialQueries,
    ): Presenter.Factory = presenterFactoryOf<HomeScreen, HomeScreen.State> { navigator, _ ->
        HomePresenter(
            remoteConfig = RemoteConfig(),
            identityManager = IdentityManager(
                platformContext = platformContext,
                credentialQueries = credentialQueries,
            ),
            navigator = navigator,
        )
    }

    fun homeUiFactory(
        platformContext: PlatformContext,
    ): Ui.Factory = uiFactoryOf<HomeScreen, HomeScreen.State> { state, modifier ->
        HomeScreen(
            state = state,
            modifier = modifier,
            reportFullyDrawn = platformContext::reportFullyDrawn,
        )
    }
}
