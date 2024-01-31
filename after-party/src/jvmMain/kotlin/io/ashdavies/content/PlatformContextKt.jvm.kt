package io.ashdavies.content

import androidx.compose.runtime.Composable

@Composable
internal actual fun getPlatformContext(): PlatformContext {
    return PlatformContext.Default
}
