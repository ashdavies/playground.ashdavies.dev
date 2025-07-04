package io.ashdavies.tally.home

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.identity.GoogleIdIdentityService
import io.ashdavies.identity.IdentityManager
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.sql.map
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface HomeModule {

    @IntoSet
    @Provides
    fun homePresenterFactory(
        platformContext: PlatformContext,
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    ): Presenter.Factory = presenterFactoryOf<HomeScreen, HomeScreen.State> { navigator, _ ->
        HomePresenter(
            platformContext = platformContext,
            remoteConfig = RemoteConfig(),
            identityManager = IdentityManager(
                identityService = GoogleIdIdentityService(platformContext),
                credentialQueries = databaseFactory.map { it.credentialQueries },
                coroutineContext = Dispatchers.Default,
            ),
            navigator = navigator,
        )
    }

    @IntoSet
    @Provides
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
