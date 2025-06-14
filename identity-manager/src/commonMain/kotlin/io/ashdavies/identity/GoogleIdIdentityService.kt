package io.ashdavies.identity

import io.ashdavies.content.PlatformContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal typealias GoogleIdIdentityService = IdentityService<GoogleIdIdentityRequest>

internal expect fun GoogleIdIdentityService(context: PlatformContext): GoogleIdIdentityService

@OptIn(ExperimentalUuidApi::class)
internal data class GoogleIdIdentityRequest(
    val serverClientId: String,
    val autoSelectEnabled: Boolean = true,
    val nonce: String = "${Uuid.random()}",
) : IdentityRequest
