package io.ashdavies.material

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
public expect fun dynamicColorScheme(darkTheme: Boolean): ColorScheme

@Composable
public fun dynamicColorScheme(): ColorScheme = dynamicColorScheme(isSystemInDarkTheme())

@Composable
internal fun defaultColorScheme(darkTheme: Boolean): ColorScheme = when (darkTheme) {
    false -> lightColorScheme()
    true -> darkColorScheme()
}
