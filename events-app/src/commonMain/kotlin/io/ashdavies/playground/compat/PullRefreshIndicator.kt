package io.ashdavies.playground.compat

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterialApi::class)
internal fun PullRefreshIndicator(
    refreshing: Boolean = false,
    state: androidx.compose.material.pullrefresh.PullRefreshState,
    modifier: Modifier = Modifier,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
) {
    val colors = androidx.compose.material.Colors(
        primary = colorScheme.primary,
        primaryVariant = colorScheme.primaryVariant,
        secondary = colorScheme.secondary,
        secondaryVariant = colorScheme.secondaryVariant,
        background = colorScheme.background,
        surface = colorScheme.surface,
        error = colorScheme.error,
        onPrimary = colorScheme.onPrimary,
        onSecondary = colorScheme.onSecondary,
        onBackground = colorScheme.onBackground,
        onSurface = colorScheme.onSurface,
        onError = colorScheme.onError,
        isLight = colorScheme.isLight,
    )

    androidx.compose.material.MaterialTheme(colors = colors) {
        androidx.compose.material.pullrefresh.PullRefreshIndicator(
            refreshing = refreshing,
            modifier = modifier,
            state = state,
        )
    }
}
