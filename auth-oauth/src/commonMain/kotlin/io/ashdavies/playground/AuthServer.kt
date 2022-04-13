package io.ashdavies.playground

import kotlinx.coroutines.flow.Flow

public expect fun beginAuthFlow(provider: OAuthProvider = OAuthProvider.Google): Flow<AccessToken>
