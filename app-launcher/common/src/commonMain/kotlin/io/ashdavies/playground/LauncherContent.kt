package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.content.PlatformContext
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.party.PlaygroundDatabase
import io.ashdavies.sql.ProvideTransacter
import io.ashdavies.sql.rememberTransacter
import okio.Path

private const val IMAGE_CACHE_PATH = "image_cache"

private const val MAX_SIZE_BYTES = 5L * 1024 * 1024
private const val MAX_SIZE_PERCENT = 0.2

@Composable
@OptIn(ExperimentalCoilApi::class)
public fun LauncherContent(context: PlatformContext, content: @Composable () -> Unit) {
    setSingletonImageLoaderFactory { ctx ->
        ImageLoader.Builder(ctx)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(ctx, MAX_SIZE_PERCENT)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.resolveCacheDir(IMAGE_CACHE_PATH))
                    .maxSizeBytes(MAX_SIZE_BYTES)
                    .build()
            }
            .build()
    }

    ProvideAppCheckToken {
        // TODO Change to io.ashdavies.common.PlaygroundDatabase
        val transacter = rememberTransacter(
            schema = PlaygroundDatabase.Schema,
            context = context,
            factory = PlaygroundDatabase.Companion::invoke,
        )

        ProvideTransacter(transacter) {
            MaterialTheme(dynamicColorScheme()) {
                content()
            }
        }
    }
}

internal expect fun PlatformContext.resolveCacheDir(
    relative: String,
): Path
