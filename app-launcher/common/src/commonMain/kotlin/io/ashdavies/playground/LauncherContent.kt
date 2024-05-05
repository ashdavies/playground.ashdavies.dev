package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.compose.ProvidePlaygroundDatabase
import io.ashdavies.content.PlatformContext
import io.ashdavies.material.dynamicColorScheme
import okio.Path

private const val IMAGE_CACHE_PATH = "image_cache"

@Composable
@OptIn(ExperimentalCoilApi::class)
public fun LauncherContent(context: PlatformContext, content: @Composable () -> Unit) {
    setSingletonImageLoaderFactory { ctx ->
        ImageLoader.Builder(ctx)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(ctx, 0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.resolveCacheDir(IMAGE_CACHE_PATH))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
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

internal expect fun PlatformContext.resolveCacheDir(
    relative: String,
): Path