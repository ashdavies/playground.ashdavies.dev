package io.ashdavies.identity

import io.ashdavies.content.PlatformContext

public object IdentityModule {
    public fun identityManager(
        platformContext: PlatformContext,
        credentialQueries: CredentialQueries,
    ): IdentityManager = IdentityManager(
        credentialQueries = credentialQueries,
        identityService = GoogleIdIdentityService(
            context = platformContext,
        ),
    )
}
