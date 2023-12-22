package io.ashdavies.identity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public actual fun IdentityManager(): IdentityManager = object : IdentityManager {
    override val state: Flow<IdentityState> = flowOf(IdentityState.Unsupported)
}
