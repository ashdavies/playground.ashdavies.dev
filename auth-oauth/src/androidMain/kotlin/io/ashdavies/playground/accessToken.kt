package io.ashdavies.playground

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual val accessToken: Flow<AccessToken>
    get() = emptyFlow()
