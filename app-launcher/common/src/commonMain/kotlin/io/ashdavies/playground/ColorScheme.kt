package io.ashdavies.playground

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
public fun dynamicColorScheme(): ColorScheme {
    return dynamicColorScheme(isSystemInDarkTheme())
}

@Composable
public expect fun dynamicColorScheme(darkTheme: Boolean): ColorScheme

@Composable
internal fun defaultColorScheme(darkTheme: Boolean = isSystemInDarkTheme()): ColorScheme {
    return if (darkTheme) darkColorScheme() else lightColorScheme()
}
