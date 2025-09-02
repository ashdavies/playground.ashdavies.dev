package dev.ashdavies.playground.identity

import dev.ashdavies.content.PlatformContext
import dev.ashdavies.identity.GoogleIdIdentityService
import dev.ashdavies.identity.IdentityManager
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface IdentityProvider {

    @Provides
    fun identityManager(
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
        platformContext: PlatformContext,
    ): IdentityManager = IdentityManager(
        identityService = GoogleIdIdentityService(platformContext),
        credentialQueries = databaseFactory.map { it.credentialQueries },
        coroutineContext = Dispatchers.Default,
    )
}
