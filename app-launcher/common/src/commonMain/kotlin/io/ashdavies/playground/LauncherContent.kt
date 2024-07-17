package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import app.cash.sqldelight.EnumColumnAdapter
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.common.PlaygroundDatabase
import io.ashdavies.content.PlatformContext
import io.ashdavies.dominion.Card
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.sql.ProvideTransacter
import io.ashdavies.sql.rememberTransacter

private const val IMAGE_CACHE_PATH = "image_cache"

private const val MAX_SIZE_BYTES = 250L * 1024 * 1024
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
        val transacter = rememberTransacter(
            schema = PlaygroundDatabase.Schema,
            context = context,
        ) { driver ->
            PlaygroundDatabase(
                driver = driver,
                CardAdapter = Card.Adapter(
                    formatAdapter = EnumColumnAdapter(),
                ),
            )
        }

        ProvideTransacter(transacter) {
            MaterialTheme(dynamicColorScheme()) {
                content()
            }
        }
    }
}
