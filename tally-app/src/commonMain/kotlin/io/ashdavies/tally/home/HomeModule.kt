package io.ashdavies.tally.home

import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import io.ashdavies.content.PlatformContext
import io.ashdavies.identity.GoogleIdIdentityService
import io.ashdavies.identity.IdentityManager
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.sql.map
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ashdavies.tally.circuit.presenterFactoryOf
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface HomeModule {

    @Provides
    fun identityManager(
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
        platformContext: PlatformContext,
    ): IdentityManager = IdentityManager(
        identityService = GoogleIdIdentityService(platformContext),
        credentialQueries = databaseFactory.map { it.credentialQueries },
        coroutineContext = Dispatchers.Default,
    )

    @IntoMap
    @Provides
    @CircuitScreenKey(HomeScreen::class)
    fun homePresenterFactory(factory: HomePresenter.Factory): Presenter.Factory {
        return presenterFactoryOf<HomeScreen, _>(factory)
    }
}
