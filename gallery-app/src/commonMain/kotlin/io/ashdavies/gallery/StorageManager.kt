package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext

internal interface StorageManager {
    fun create(context: PlatformContext): File
    fun list(): List<File>
    fun delete(file: File): Boolean
}

internal expect fun StorageManager(
    parent: File,
): StorageManager
