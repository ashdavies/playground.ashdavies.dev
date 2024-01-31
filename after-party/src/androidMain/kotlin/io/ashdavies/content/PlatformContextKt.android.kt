package io.ashdavies.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun getPlatformContext(): PlatformContext {
    return LocalContext.current
}
