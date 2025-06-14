package io.ashdavies.http

import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import okio.Path

public fun HttpCache.Config.publicStorage(path: Path) {
    publicStorage(FileStorage(path.toFile()))
}
