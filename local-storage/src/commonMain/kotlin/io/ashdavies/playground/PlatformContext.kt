package io.ashdavies.playground

import androidx.compose.runtime.Composable
import io.ashdavies.content.PlatformContext

@Composable
internal expect fun getPlatformContext(): PlatformContext
