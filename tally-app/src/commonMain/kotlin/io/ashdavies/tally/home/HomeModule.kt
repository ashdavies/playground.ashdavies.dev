package io.ashdavies.tally.home

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.ashdavies.content.PlatformContext
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

    @Provides
    fun identityManager(
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
        platformContext: PlatformContext,
    ): IdentityManager = IdentityManager(
        identityService = GoogleIdIdentityService(platformContext),
        credentialQueries = databaseFactory.map { it.credentialQueries },
        coroutineContext = Dispatchers.Default,
    )

    @IntoSet
    @Provides
    fun homePresenterFactory(factory: HomePresenter.Factory): Presenter.Factory {
        return presenterFactoryOf<HomeScreen, _>(factory)
    }

    @IntoSet
    @Provides
    fun homeUiFactory(provider: Provider<HomeUi>): Ui.Factory {
        return uiFactoryOf<HomeScreen, _>(provider)
    }
}
