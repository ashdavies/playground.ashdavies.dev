package io.ashdavies.identity

import kotlinx.coroutines.flow.Flow

public interface IdentityManager {
    public val state: Flow<IdentityState>
}

public expect fun IdentityManager(): IdentityManager
