package io.ashdavies.identity

import io.ashdavies.content.PlatformContext
import kotlinx.coroutines.flow.Flow

public interface IdentityManager {
    public val state: Flow<IdentityState>
}

public expect fun IdentityManager(
    context: PlatformContext,
): IdentityManager
