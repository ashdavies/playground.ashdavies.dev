package io.ashdavies.playground

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
public actual fun dynamicColorScheme(darkTheme: Boolean): ColorScheme = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicColorScheme(
        context = LocalContext.current,
        darkTheme = darkTheme,
    )

    else -> defaultColorScheme(darkTheme)
}

@Composable
@RequiresApi(Build.VERSION_CODES.S)
private fun dynamicColorScheme(context: Context, darkTheme: Boolean): ColorScheme = when {
    darkTheme -> dynamicDarkColorScheme(context)
    else -> dynamicLightColorScheme(context)
}
