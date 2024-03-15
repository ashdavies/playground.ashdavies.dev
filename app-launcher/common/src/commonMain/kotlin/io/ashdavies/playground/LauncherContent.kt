package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.compose.ProvidePlaygroundDatabase
import io.ashdavies.content.PlatformContext
import io.ashdavies.material.dynamicColorScheme

@Composable
@OptIn(ExperimentalCoilApi::class)
public fun LauncherContent(context: PlatformContext, content: @Composable () -> Unit) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .build()
    }

    ProvideAppCheckToken {
        ProvidePlaygroundDatabase(context) {
            MaterialTheme(dynamicColorScheme()) {
                content()
            }
        }
    }
}
