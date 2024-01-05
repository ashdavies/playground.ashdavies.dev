package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.fetch.NetworkFetcher
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.material.dynamicColorScheme

@Composable
@OptIn(ExperimentalCoilApi::class)
public fun LauncherContent(content: @Composable () -> Unit) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(NetworkFetcher.Factory()) }
            .build()
    }

    ProvideAppCheckToken {
        MaterialTheme(dynamicColorScheme()) {
            content()
        }
    }
}
