package io.ashdavies.material

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
public actual fun dynamicColorScheme(darkTheme: Boolean): ColorScheme = when {
    darkTheme -> dynamicDarkColorScheme(LocalContext.current)
    else -> dynamicLightColorScheme(LocalContext.current)
}
