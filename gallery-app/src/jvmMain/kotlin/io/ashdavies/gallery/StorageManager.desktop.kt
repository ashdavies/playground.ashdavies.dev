package io.ashdavies.gallery

import java.io.File
import java.util.UUID

internal actual fun StorageManager(parent: File): StorageManager = object : StorageManager {

    override fun create(context: Context): File {
        val file = File(parent, "${UUID.randomUUID()}.jpg")
        if (!file.createNewFile()) throw IllegalStateException()
        return file
    }

    override fun list(): List<File> {
        val files = parent.listFiles() ?: return emptyList()
        return files.toList()
    }

    override fun delete(file: File): Boolean {
        if (!file.exists()) throw IllegalArgumentException()
        return file.delete()
    }
}
