package io.ashdavies.identity

import io.ashdavies.content.PlatformContext

internal actual class GoogleIdIdentityService actual constructor(context: PlatformContext) : IdentityService<GoogleIdIdentityRequest> {
    override suspend fun request(request: GoogleIdIdentityRequest): IdentityResponse {
        throw UnsupportedOperationException()
    }
}
