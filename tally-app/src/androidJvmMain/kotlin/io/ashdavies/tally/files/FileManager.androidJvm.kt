package io.ashdavies.tally.files

import kotlinx.io.Source
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

internal actual fun FileManager() = object : FileManager {
    override fun readByteArray(path: Path): ByteArray {
        return (SystemFileSystem.source(path) as Source).readByteArray()
    }

    override fun delete(path: Path) {
        SystemFileSystem.delete(path)
    }
}
