package io.ashdavies.playground.check

import kotlinx.serialization.Serializable

@Serializable
internal data class VerifyAppCheckTokenResponse(val token: DecodedAppCheckToken)
