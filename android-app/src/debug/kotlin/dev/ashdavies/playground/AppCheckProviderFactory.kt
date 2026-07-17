package dev.ashdavies.playground

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

internal fun AppCheckProviderFactory(): AppCheckProviderFactory {
    return DebugAppCheckProviderFactory.getInstance()
}
