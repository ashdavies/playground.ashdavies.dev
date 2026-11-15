package dev.ashdavies.http

import io.ktor.client.plugins.cache.storage.CacheStorage

internal actual fun resolveCacheStorage(): CacheStorage {
    return CacheStorage.Unlimited()
}
