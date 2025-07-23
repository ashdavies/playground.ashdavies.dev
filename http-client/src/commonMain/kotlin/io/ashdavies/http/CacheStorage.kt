package io.ashdavies.http

import io.ktor.client.plugins.cache.storage.CacheStorage

internal expect fun resolveCacheStorage(): CacheStorage
