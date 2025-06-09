package io.ashdavies.material

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
public actual fun dynamicColorScheme(darkTheme: Boolean): ColorScheme = defaultColorScheme(darkTheme)
