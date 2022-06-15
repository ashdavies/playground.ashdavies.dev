package io.ashdavies.playground

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalAppCheckToken: ProvidableCompositionLocal<String> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalAppCheckToken") }

private fun noLocalProvidedFor(name: String): Nothing =
    error("CompositionLocal $name not present")
