package dev.ashdavies.identity

import io.ashdavies.content.PlatformContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public typealias GoogleIdIdentityService = IdentityService<GoogleIdIdentityRequest>

public expect fun GoogleIdIdentityService(context: PlatformContext): GoogleIdIdentityService

@OptIn(ExperimentalUuidApi::class)
public data class GoogleIdIdentityRequest(
    internal val serverClientId: String,
    internal val autoSelectEnabled: Boolean = true,
    internal val nonce: String = "${Uuid.random()}",
) : IdentityRequest
