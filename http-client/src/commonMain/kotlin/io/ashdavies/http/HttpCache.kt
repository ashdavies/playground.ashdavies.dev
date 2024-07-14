package io.ashdavies.http

import io.ktor.client.plugins.cache.HttpCache.Config
import io.ktor.client.plugins.cache.storage.FileStorage
import java.nio.file.Files
import java.nio.file.Paths

public fun Config.publicFileStorage(path: String = "build/cache") {
    val cachePath = Files.createDirectories(Paths.get(path))
    publicStorage(FileStorage(cachePath.toFile()))
}
