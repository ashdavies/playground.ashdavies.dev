package io.ashdavies.playground

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

public val LocalAppCheckToken: ProvidableCompositionLocal<AppCheckToken> =
    compositionLocalOf { noLocalProvidedFor("LocalAppCheckToken") }

private fun noLocalProvidedFor(name: String): Nothing =
    error("CompositionLocal $name not present")
