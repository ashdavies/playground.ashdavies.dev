package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil3.EventListener
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.common.PlaygroundDatabase
import io.ashdavies.content.PlatformContext
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.sql.ProvideTransacter
import io.ashdavies.sql.rememberTransacter

private const val IMAGE_CACHE_PATH = "image_cache"

private const val MAX_SIZE_BYTES = 250L * 1024 * 1024
private const val MAX_SIZE_PERCENT = 0.2

@Composable
public fun LauncherContent(context: PlatformContext, content: @Composable () -> Unit) {
    setSingletonImageLoaderFactory { ctx ->
        ImageLoader.Builder(ctx)
            .eventListener(
                object : EventListener() {
                    override fun onError(request: ImageRequest, result: ErrorResult) {
                        println("Error loading image: ${request.data}: $result")
                        result.throwable.printStackTrace()
                    }
                },
            )
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
            )
        }

        ProvideTransacter(transacter) {
            MaterialTheme(dynamicColorScheme()) {
                content()
            }
        }
    }
}
