package io.ashdavies.gallery

internal actual fun StorageManager(paths: PathProvider): StorageManager = object : StorageManager {
    override fun createFileUri(context: Context): Uri = throw UnsupportedOperationException("Not Supported")
    override fun listFilesAsUri(): List<Uri> = throw UnsupportedOperationException("Not Supported")
    override fun delete(value: Uri): Boolean = throw UnsupportedOperationException("Not Supported")
}
