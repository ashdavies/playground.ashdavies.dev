package dev.ashdavies.playground

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

internal fun AppCheckProviderFactory(): AppCheckProviderFactory {
    return PlayIntegrityAppCheckProviderFactory.getInstance()
}
