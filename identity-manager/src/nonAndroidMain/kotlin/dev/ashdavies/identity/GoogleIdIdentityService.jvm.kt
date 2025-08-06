package dev.ashdavies.identity

import io.ashdavies.content.PlatformContext

public actual fun GoogleIdIdentityService(context: PlatformContext): GoogleIdIdentityService {
    return GoogleIdIdentityService { _ -> throw UnsupportedOperationException() }
}
