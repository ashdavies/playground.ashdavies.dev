package io.ashdavies.playground.compose

import androidx.compose.runtime.Composable
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.imageloading.LoadPainter
import com.google.accompanist.imageloading.rememberLoadPainter
import kotlinx.coroutines.flow.flowOf

internal val LoadPainter<*>.isLoading: Boolean
    get() = loadState is ImageLoadState.Loading

@Composable
internal fun rememberEmptyPainter(): LoadPainter<Any> = rememberLoadPainter(
    loader = { _, _ -> flowOf(ImageLoadState.Empty) },
    shouldRefetchOnSizeChange = { _, _ -> false },
    request = null,
)