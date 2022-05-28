package io.ashdavies.playground

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
public fun PlaygroundTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(dynamicColorScheme(darkTheme), content = content)
}

@Composable
public expect fun dynamicColorScheme(darkTheme: Boolean): ColorScheme

@Composable
public fun defaultColorScheme(darkTheme: Boolean): ColorScheme {
    return if (darkTheme) darkColorScheme() else lightColorScheme()
}
