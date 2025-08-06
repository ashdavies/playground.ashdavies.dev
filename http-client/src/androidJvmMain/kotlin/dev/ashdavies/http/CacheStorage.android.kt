package dev.ashdavies.http

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import kotlinx.io.files.Path
import kotlinx.io.files.SystemTemporaryDirectory
import java.io.File

internal actual fun resolveCacheStorage(): CacheStorage {
    val cachePath = Path(SystemTemporaryDirectory, "http-cache")
    return FileStorage(File(cachePath.toString()))
}
