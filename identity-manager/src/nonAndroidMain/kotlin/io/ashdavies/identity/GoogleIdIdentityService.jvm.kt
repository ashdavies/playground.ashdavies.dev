package io.ashdavies.identity

import io.ashdavies.content.PlatformContext

internal actual fun GoogleIdIdentityService(
    context: PlatformContext,
): GoogleIdIdentityService = GoogleIdIdentityService { request ->
    throw UnsupportedOperationException()
}
