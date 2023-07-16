package io.ashdavies.gallery

import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.File
import java.util.UUID

internal actual fun StorageManager(paths: PathProvider): StorageManager = object : StorageManager {

    override fun createFileUri(context: Context): Uri {
        val file = File(paths.images.toFile(), "${UUID.randomUUID()}.jpg").apply { createNewFile() }
        val authority = "${context.packageName}.files"

        return FileProvider.getUriForFile(context, authority, file)
    }

    override fun listFilesAsUri(): List<Uri> = with(paths.images.toFile()) {
        return listFiles()?.map { it.toUri() } ?: emptyList()
    }

    override fun delete(value: Uri): Boolean = with(value.toFile()) {
        if (exists()) delete() else throw IllegalArgumentException()
    }
}
