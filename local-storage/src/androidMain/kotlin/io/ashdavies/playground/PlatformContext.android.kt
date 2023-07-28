package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.ashdavies.content.PlatformContext

@Composable
internal actual fun getPlatformContext(): PlatformContext {
    return LocalContext.current
}
