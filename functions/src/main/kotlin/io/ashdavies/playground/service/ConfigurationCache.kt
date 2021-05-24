package io.ashdavies.playground.service

import io.ashdavies.playground.collection.CollectionCache
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.store.Cache

private const val CONFIGURATION = "configuration"

private val Admin.configuration: CollectionReference<Configuration>
    get() = firestore().collection(CONFIGURATION)

internal fun ConfigurationCache(): Cache<Configuration.Type, Configuration> {
    return CollectionCache(Admin.configuration)
}

internal data class Configuration(val updatedAt: String? = null) {
    sealed class Type(override val name: String) : CollectionCache.Type {
        companion object Default : Type("default")
    }
}