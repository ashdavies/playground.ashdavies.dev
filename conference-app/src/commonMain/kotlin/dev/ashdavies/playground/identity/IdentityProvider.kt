package dev.ashdavies.playground.identity

import dev.ashdavies.content.PlatformContext
import dev.ashdavies.identity.GoogleIdIdentityService
import dev.ashdavies.identity.IdentityManager
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.playground.metro.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SuspendLazy
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface IdentityProvider {

    @Provides
    @ExperimentalMetroCoroutinesApi
    fun identityManager(
        databaseFactory: SuspendLazy<PlaygroundDatabase>,
        platformContext: PlatformContext,
    ): IdentityManager = IdentityManager(
        identityService = GoogleIdIdentityService(platformContext),
        credentialQueries = databaseFactory.map { it.credentialQueries },
        coroutineContext = Dispatchers.Default,
    )
}
