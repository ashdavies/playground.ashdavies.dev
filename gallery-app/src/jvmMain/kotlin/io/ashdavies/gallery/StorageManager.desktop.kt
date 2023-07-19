package io.ashdavies.gallery

internal actual fun StorageManager(parent: File): StorageManager = object : StorageManager {
    override fun create(context: Context): File = throw UnsupportedOperationException("Not Supported")
    override fun list(): List<File> = throw UnsupportedOperationException("Not Supported")
    override fun delete(file: File): Boolean = throw UnsupportedOperationException("Not Supported")
}
