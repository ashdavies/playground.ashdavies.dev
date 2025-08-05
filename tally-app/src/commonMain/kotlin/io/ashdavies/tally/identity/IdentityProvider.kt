package io.ashdavies.tally.identity

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ashdavies.content.PlatformContext
import io.ashdavies.identity.GoogleIdIdentityService
import io.ashdavies.identity.IdentityManager
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.sql.map
import io.ashdavies.tally.PlaygroundDatabase
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
