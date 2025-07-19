package io.ashdavies.tally.files

internal actual fun FileManager() = object : FileManager {
    override fun readByteArray(path: Path) = byteArrayOf()
    override fun delete(path: Path) = Unit
}
