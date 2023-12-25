package io.ashdavies.identity

import io.ashdavies.content.PlatformContext
import io.ashdavies.util.randomUuid

internal expect class GoogleIdIdentityService(
    context: PlatformContext
) : IdentityService<GoogleIdIdentityRequest>

internal data class GoogleIdIdentityRequest(
    val serverClientId: String,
    val filterByAuthorizedAccounts: Boolean = true,
    val autoSelectEnabled: Boolean = true,
    val nonce: String? = randomUuid(),
) : IdentityRequest
