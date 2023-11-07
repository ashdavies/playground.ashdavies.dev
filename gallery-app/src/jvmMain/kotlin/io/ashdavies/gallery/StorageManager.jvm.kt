package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext
import java.io.File

internal actual fun StorageManager(parent: File): StorageManager = object : StorageManager {

    override fun create(context: PlatformContext): File {
        val file = File(parent, "${randomUuid()}.jpg")
        if (!file.createNewFile()) throw IllegalStateException()
        return file
    }

    override fun delete(file: File): Boolean {
        return file.delete()
    }
}
