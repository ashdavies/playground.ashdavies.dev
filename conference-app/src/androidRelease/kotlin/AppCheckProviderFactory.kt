package dev.ashdavies.playground

import com.google.firebase.appcheck.AppCheckProviderFactory

internal fun AppCheckProviderFactory(): AppCheckProviderFactory {
    return AppCheckProviderFactory.getInstance()
}
