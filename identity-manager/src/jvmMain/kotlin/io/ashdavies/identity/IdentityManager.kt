package io.ashdavies.identity

import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public actual fun IdentityManager(context: PlatformContext): IdentityManager = object : IdentityManager {
    override val state: Flow<IdentityState> = flowOf(IdentityState.Unsupported)
}
