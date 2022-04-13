package io.ashdavies.playground

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public actual fun beginAuthFlow(provider: OAuthProvider): Flow<AccessToken> = emptyFlow()
